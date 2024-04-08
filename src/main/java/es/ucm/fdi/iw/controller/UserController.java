package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.House;
import es.ucm.fdi.iw.model.Room;
import es.ucm.fdi.iw.model.Task;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Reactive.Session;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.crypto.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Exception to use when denying access to unauthorized users.
	 * 
	 * In general, admins are always authorized, but users cannot modify
	 * each other's profiles.
	 */
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No eres administrador, y este no es tu perfil") // 403
	public static class NoEsTuPerfilException extends RuntimeException {
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * 
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 *         for example, a possible encoding of "test" is
	 *         {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	/**
	 * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
	 * 
	 * @param byteLength
	 * @return
	 */
	public static String generateRandomBase64Token(int byteLength) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[byteLength];
		secureRandom.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // base64 encoding
	}

	// --------------------------------------------------------------------------------------------------------------------

	// GETTERS ----------------------------------
	@GetMapping("/filterRoom/{id}")
	@ResponseBody
	public String filterRoom(@PathVariable long id, HttpServletResponse response) {
		List<Task> tasks = entityManager
				.createNamedQuery("Task.byRoom", Task.class)
				.setParameter("roomId", id)
				.getResultList();

		StringBuilder jsonResult = new StringBuilder("{");

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			jsonResult.append("\"").append(task.getId()).append("\": {");
			jsonResult.append("\"title\": \"").append(task.getTitle()).append("\",");
			jsonResult.append("\"author\": \"").append(task.getAuthor()).append("\",");
			jsonResult.append("\"creationDate\": \"").append(task.getCreationDate()).append("\",");
			jsonResult.append("\"user\": \"").append(task.getUser().getUsername()).append("\",");
			jsonResult.append("\"id\": \"").append(task.getId()).append("\",");
			jsonResult.append("\"room\": \"").append(task.getRoom().getName()).append("\"}");

			if (i < tasks.size() - 1) {
				jsonResult.append(", ");
			}
		}

		jsonResult.append("}");
		return jsonResult.toString();
	}

	@GetMapping("/filterUser/{id}")
	@ResponseBody
	public String filterUser(@PathVariable long id, HttpServletResponse response) {

		User aux = entityManager.find(User.class, id);
		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("userId", aux)
				.getResultList();

		StringBuilder jsonResult = new StringBuilder("{");

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			jsonResult.append("\"").append(task.getId()).append("\": {");
			jsonResult.append("\"title\": \"").append(task.getTitle()).append("\",");
			jsonResult.append("\"author\": \"").append(task.getAuthor()).append("\",");
			jsonResult.append("\"creationDate\": \"").append(task.getCreationDate()).append("\",");
			jsonResult.append("\"user\": \"").append(task.getUser().getUsername()).append("\",");
			jsonResult.append("\"id\": \"").append(task.getId()).append("\",");
			jsonResult.append("\"room\": \"").append(task.getRoom().getName()).append("\"}");

			if (i < tasks.size() - 1) {
				jsonResult.append(", ");
			}
		}

		jsonResult.append("}");
		return jsonResult.toString();
	}

	@GetMapping("/getTaskInfo/{id}")
	@ResponseBody
	public String getInfoTask(@PathVariable long id, HttpServletResponse response) {
		Task target = entityManager.createNamedQuery("Task.byId", Task.class)
				.setParameter("taskId", id)
				.getSingleResult();

		return "{\"title\": \"" + target.getTitle() + "\"," +
				"\"author\": \"" + target.getAuthor() + "\"," +
				"\"creationDate\": \"" + target.getCreationDate() + "\"," +
				"\"user\": \"" + target.getUser().getUsername() + "\"," +
				"\"room\": \"" + target.getRoom().getName() + "\"}";
	}

	@GetMapping("/home1")
	public String TM_home1(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/home2";
		}

		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("userId", u)
				.getResultList();

		model.addAttribute("tasks", tasks);
		model.addAttribute("u", u);

		return "TM_home1";
	}

	@GetMapping("/home2")
	public String TM_home2(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");
		// En caso de no tener casa asignada
		if (u.getHouse() != null) {
			return "redirect:/user/home1";
		}

		model.addAttribute("u", u);

		return "TM_home2";
	}

	@GetMapping("/task")
	@Transactional
	public String TM_tareas(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");
		House h = u.getHouse();

		// En caso de no tener casa asignada
		if (h == null) {
			return "redirect:/user/home2";
		}

		House target = entityManager.find(House.class, h.getId());
		List<Task> tasks = entityManager
				.createNamedQuery("Task.forHouse", Task.class)
				.setParameter("house", target)
				.getResultList();

		model.addAttribute("houseUsers", target.getUsers());
		model.addAttribute("rooms", target.getRooms());
		model.addAttribute("authorName", u.getUsername());

		model.addAttribute("tasks", tasks);
		model.addAttribute("u", u);

		return "TM_tasks";
	}

	@GetMapping("/manager")
	@Transactional
	public String TM_jefe(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/home2";
		}

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return "redirect:/user/home1";
		}

		House h = u.getHouse();
		House target = entityManager.find(House.class, h.getId());

		model.addAttribute("membersHouse", target.getUsers());
		model.addAttribute("rooms", target.getRooms());
		model.addAttribute("u", u);

		return "TM_manager";
	}

	@GetMapping("/expenses")
	public String TM_gastos(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/home2";
		}

		model.addAttribute("u", u);

		return "TM_expenses";
	}

	// POSTS ----------------------------------

	@PostMapping("/newTask")
	@Transactional
	@ResponseBody
	public String newTask(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		String title = data.get("title").asText();
		long room_id = data.get("room_id").asLong();
		long user_id = data.get("user_id").asLong();

		Task target = new Task();
		target.setTitle(title);
		target.setAuthor(((User) session.getAttribute("u")).getUsername());
		target.setRoom(entityManager.find(Room.class, room_id));
		target.setUser(entityManager.find(User.class, user_id));
		target.setEnabled(true);

		long ms = System.currentTimeMillis();

		// TODO Fecha no actualizada
		target.setCreationDate(new Date(ms));

		entityManager.persist(target);
		entityManager.flush(); // forces DB to add user & assign valid id

		return "{\"title\": \"" + target.getTitle() + "\"," +
				"\"author\": \"" + target.getAuthor() + "\"," +
				"\"creationDate\": \"" + target.getCreationDate() + "\"," +
				"\"user\": \"" + target.getUser() + "\"," +
				"\"id\": \"" + target.getId() + "\"," +
				"\"room\": \"" + target.getRoom().getName() + "\"}";
	}

	@PostMapping("/updateTask")
	@Transactional
	@ResponseBody
	public String updateTask(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		long task_id = data.get("id").asLong();
		String title = data.get("title").asText();
		long room_id = data.get("room_id").asLong();
		long user_id = data.get("user_id").asLong();

		Task target = new Task();
		target = entityManager.find(Task.class, task_id);
		target.setTitle(title);
		target.setRoom(entityManager.find(Room.class, room_id));
		target.setEnabled(true);


		entityManager.flush(); // forces DB to add user & assign valid id

		return "{\"title\": \"" + target.getTitle() + "\"," +
				"\"author\": \"" + target.getAuthor() + "\"," +
				"\"creationDate\": \"" + target.getCreationDate() + "\"}" ;
		

	}			

	@PostMapping("/newHouse")
	@Transactional
	public String newHouse(
			HttpServletResponse response,
			@RequestParam String houseName,
			@RequestParam String housePassword,
			Model model, HttpSession session) throws IOException {

		// Crear casa
		House houseNew = new House();
		houseNew.setName(houseName);
		houseNew.setEnabled(true);
		houseNew.setPass(encodePassword(housePassword));

		entityManager.persist(houseNew);
		entityManager.flush();

		// Usuario -> Manager
		User u = (User) session.getAttribute("u");
		User user = entityManager.createNamedQuery("User.byUsername", User.class)
				.setParameter("username", u.getUsername())
				.getSingleResult();

		user.setRoles(Role.MANAGER.name());
		user.setHouse(houseNew);

		entityManager.persist(user);
		entityManager.flush();

		session.setAttribute("u", user);

		return "redirect:/user/manager";
	}

	@PostMapping("/newRoom")
	@Transactional
	@ResponseBody
	public String newRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		Room roomNew = new Room();

		User u = (User) session.getAttribute("u");
		User user = entityManager.createNamedQuery("User.byUsername", User.class)
				.setParameter("username", u.getUsername())
				.getSingleResult();
		House h = user.getHouse();

		// Room existingRoom = entityManager.createNamedQuery("Room.byroomname",
		// Room.class)
		// .setParameter("roomName", roomName)
		// .getSingleResult();

		// if (existingRoom == null || existingRoom.getHouse() != h) {
		String roomName = data.get("roomName").asText();

		roomNew.setName(roomName);
		roomNew.setHouse(h);
		roomNew.setEnabled(true);
		entityManager.persist(roomNew);
		entityManager.flush();
		// }

		return "{\"name\": \"" + roomNew.getName() + "\"}";
		// return "redirect:/user/manager";
	}

	@PostMapping("/joinHouse")
	@Transactional
	public String joinHouse(
			HttpServletResponse response,
			@RequestParam String JhouseName,
			@RequestParam String JhousePassword,
			Model model, HttpSession session) throws IOException {

		User u = (User) session.getAttribute("u");
		User user = entityManager.createNamedQuery("User.byUsername", User.class)
				.setParameter("username", u.getUsername())
				.getSingleResult();

		// Comprobar existencia de la casa

		House h = entityManager.createNamedQuery("House.byHousename", House.class)
				.setParameter("name", JhouseName)
				.getSingleResult();

		// COMPROBAR QUE LA PASSWORD DE LA CASA ES IGUAL QUE LA INTRODUCIDA
		if (passwordEncoder.matches(JhousePassword, h.getPass())) {
			user.setHouse(h);

		} else {
			return "redirect:/user/home2";
		}

		entityManager.persist(user);
		entityManager.flush();
		session.setAttribute("u", user);

		return "redirect:/user/home1";
	}

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * Landing page for a user profile
	 */
	@GetMapping("{id}")
	public String index(@PathVariable long id, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);
		return "user";
	}

	/**
	 * Alter or create a user
	 */
	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id,
			@ModelAttribute User edited,
			@RequestParam(required = false) String pass2,
			Model model, HttpSession session) throws IOException {

		User requester = (User) session.getAttribute("u");
		User target = null;
		if (id == -1 && requester.hasRole(Role.ADMIN)) {
			// create new user with random password
			target = new User();
			target.setPassword(encodePassword(generateRandomBase64Token(12)));
			target.setEnabled(true);
			entityManager.persist(target);
			entityManager.flush(); // forces DB to add user & assign valid id
			id = target.getId(); // retrieve assigned id from DB
		}

		// retrieve requested user
		target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		if (edited.getPassword() != null) {
			if (!edited.getPassword().equals(pass2)) {
				// FIXME: complain
			} else {
				// save encoded version of password
				target.setPassword(encodePassword(edited.getPassword()));
			}
		}
		target.setUsername(edited.getUsername());

		// update user session so that changes are persisted in the session, too
		if (requester.getId() == target.getId()) {
			session.setAttribute("u", target);
		}

		return "user";
	}

	/**
	 * Returns the default profile pic
	 * 
	 * @return
	 */
	private static InputStream defaultPic() {
		return new BufferedInputStream(Objects.requireNonNull(
				UserController.class.getClassLoader().getResourceAsStream(
						"static/img/default-pic.jpg")));
	}

	/**
	 * Downloads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("{id}/pic")
	public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
		File f = localData.getFile("user", "" + id + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : UserController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}

	/**
	 * Uploads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PostMapping("{id}/pic")
	@ResponseBody
	public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException {

		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		// check permissions
		User requester = (User) session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", "" + id + ".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
	}
}
