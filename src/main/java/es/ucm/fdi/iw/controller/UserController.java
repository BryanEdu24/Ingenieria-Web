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
	public List<Task.Transfer> filterRoom(@PathVariable long id, HttpServletResponse response) {
		List<Task> tasks = entityManager
				.createNamedQuery("Task.byRoom", Task.class)
				.setParameter("roomId", id)
				.getResultList();

		List<Task.Transfer> filterByRoomList = new ArrayList<Task.Transfer>();

		for (Task t : tasks) {
			filterByRoomList.add(t.toTransfer());
		}

		return filterByRoomList;
	}

	@GetMapping("/filterUser/{id}")
	@ResponseBody
	public List<Task.Transfer> filterUser(@PathVariable long id, HttpServletResponse response) {

		User aux = entityManager.find(User.class, id);
		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("userId", aux)
				.getResultList();

		List<Task.Transfer> filterByUserList = new ArrayList<Task.Transfer>();

		for (Task t : tasks) {
			filterByUserList.add(t.toTransfer());
		}

		return filterByUserList;
	}

	@GetMapping("/getTaskInfo/{id}")
	@ResponseBody
	public Task.Transfer getInfoTask(@PathVariable long id, HttpServletResponse response) {
		Task target = entityManager.createNamedQuery("Task.byId", Task.class)
				.setParameter("taskId", id)
				.getSingleResult();

		return target.toTransfer();
	}

	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("userId", u)
				.getResultList();

		model.addAttribute("tasks", tasks);
		model.addAttribute("u", u);

		return "home";
	}

	@GetMapping("/welcome")
	public String welcome(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");
		// En caso de no tener casa asignada
		if (u.getHouse() != null) {
			return "redirect:/user/home";
		}

		model.addAttribute("u", u);

		return "welcome";
	}

	@GetMapping("/tasks")
	@Transactional
	public String task(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");
		House h = u.getHouse();

		// En caso de no tener casa asignada
		if (h == null) {
			return "redirect:/user/welcome";
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

		return "tasks";
	}

	@GetMapping("/manager")
	@Transactional
	public String manager(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return "redirect:/user/home";
		}

		House h = u.getHouse();
		House target = entityManager.find(House.class, h.getId());

		List<Room> rooms = target.getRooms();

		model.addAttribute("membersHouse", target.getUsers());
		model.addAttribute("rooms", rooms);
		model.addAttribute("u", u);

		return "manager";
	}

	@GetMapping("/expenses")
	public String expenses(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		model.addAttribute("u", u);

		return "expenses";
	}

	// POSTS ----------------------------------

	@PostMapping("/newTask")
	@Transactional
	@ResponseBody
	public Task.Transfer newTask(
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

		return target.toTransfer();
	}

	@PostMapping("/updateTask")
	@Transactional
	@ResponseBody
	public Task.Transfer updateTask(
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
		target.setUser(entityManager.find(User.class, user_id));
		target.setRoom(entityManager.find(Room.class, room_id));
		target.setEnabled(true);

		entityManager.persist(target);
		entityManager.flush(); // forces DB to add user & assign valid id

		return target.toTransfer();
	}

	@PostMapping("/deleteTask")
	@Transactional
	@ResponseBody
	public Task.Transfer deleteTask(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) {
		// TODO: process POST request
		long task_id = data.get("id").asLong();
		Task target = new Task();
		target = entityManager.find(Task.class, task_id);

		target.setEnabled(false);
		entityManager.persist(target);
		entityManager.flush();

		return target.toTransfer();
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
	public Room.Transfer newRoom(
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
		String roomPhoto = data.get("roomPhoto").asText();

		roomNew.setName(roomName);
		roomNew.setImg(roomPhoto);
		roomNew.setHouse(h);
		roomNew.setEnabled(true);

		entityManager.persist(roomNew);
		entityManager.flush();
		// }

		return roomNew.toTransfer();
		// return "redirect:/user/manager";
	}

	@PostMapping("/updateRoom")
	@Transactional
	@ResponseBody
	public Room.Transfer updateRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		String roomName = data.get("name").asText(); // Obtén el nuevo nombre de la habitación
		long roomId = data.get("id").asLong(); // Obtén el ID de la habitación
		Room roomToUpdate = entityManager.find(Room.class, roomId); // Encuentra la habitación en la base de datos
		roomToUpdate.setName(roomName); // Actualiza el nombre de la habitación

		entityManager.persist(roomToUpdate); // Persiste los cambios en la base de datos
		entityManager.flush();

		return roomToUpdate.toTransfer(); // Devuelve los datos actualizados de la habitación
	}

	@PostMapping("/deleteRoom")
	@Transactional
	@ResponseBody
	public Room.Transfer deleteRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		// Obtén el nuevo nombre de la habitación
		long roomId = data.get("id").asLong(); // Obtén el ID de la habitación
		Room roomToDelete = entityManager.find(Room.class, roomId); // Encuentra la habitación en la base de datos
		roomToDelete.setEnabled(false);
		entityManager.persist(roomToDelete); // Persiste los cambios en la base de datos
		entityManager.flush();

		return roomToDelete.toTransfer(); // Devuelve los datos actualizados de la habitación
	}

	@PostMapping("/deleteUser")
	@Transactional
	@ResponseBody
	public User.Transfer deleteUser(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		// Obtén el nuevo nombre de la habitación
		long userId = data.get("id").asLong(); // Obtén el ID del usuario
		User userToDelete = entityManager.find(User.class, userId); // Encuentra el usuario en la base de datos

		// Desvincula al usuario de la casa
		userToDelete.setHouse(null);

		entityManager.persist(userToDelete);
		entityManager.flush();
		return userToDelete.toTransfer(); // Devuelve los datos actualizados de la habitación
	}

	@PostMapping("/deleteHouse")
	@Transactional
	@ResponseBody
	public House.Transfer deleteHouse(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		// Obtén el nuevo nombre de la habitación
		long house_id = data.get("id").asLong(); // Obtén el ID del usuario
		House house = entityManager.find(House.class, house_id); // Encuentra el usuario en la base de datos

		// // Desvincula al usuario de la casa
		// List<User> users = house.getUsers();

		// for (User u : users) {
		// u.setHouse(null);
		// entityManager.persist(u);
		// // users.remove(i);
		// }

		house.setEnabled(false);
		entityManager.persist(house);
		entityManager.flush();
		return house.toTransfer(); // Devuelve los datos actualizados de la habitación
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
			return "redirect:/user/welcome";
		}

		entityManager.persist(user);
		entityManager.flush();
		session.setAttribute("u", user);

		return "redirect:/user/home";
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
