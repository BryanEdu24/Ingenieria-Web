package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.House;
import es.ucm.fdi.iw.model.Note;
import es.ucm.fdi.iw.model.Notification;
import es.ucm.fdi.iw.model.Room;
import es.ucm.fdi.iw.model.Task;
import es.ucm.fdi.iw.model.Expense;
import es.ucm.fdi.iw.model.Historical;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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

	// ----- GETs -----
	// Enrutamiento
	// Cargar vista de home
	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("user", u)
				.getResultList();

		model.addAttribute("tasks", tasks);
		model.addAttribute("u", u);

		return "home";
	}

	// Cargar vista historical
	@GetMapping("/historical")
	public String historical(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		List<Historical> historicals = entityManager
				.createNamedQuery("Historical.byHouse", Historical.class)
				.setParameter("house", entityManager.find(House.class, u.getHouse().getId()))
				.getResultList();

		model.addAttribute("historicals", historicals);
		model.addAttribute("u", u);

		return "historical";
	}

	// Cargar vista de welcome
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

	// Cargar vista de tasks
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

	// Cargar vista de manager
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

		model.addAttribute("currHouse", target);
		model.addAttribute("membersHouse", target.getUsers());
		model.addAttribute("rooms", rooms);
		model.addAttribute("u", u);

		return "manager";
	}

	// Cargar vista de expenses
	@GetMapping("/expenses")
	public String expenses(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return "redirect:/user/welcome";
		}

		List<Expense> expenses = entityManager
				.createNamedQuery("Expense.byHouse", Expense.class)
				.setParameter("house", entityManager.find(House.class, u.getHouse().getId()))
				.getResultList();

		model.addAttribute("u", u);
		model.addAttribute("expenses", expenses);

		return "expenses";
	}

	// ------ Otros GETs -----
	// Filtro por habitación
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

	// Filtro por usuario
	@GetMapping("/filterUser/{id}")
	@ResponseBody
	public List<Task.Transfer> filterUser(@PathVariable long id, HttpServletResponse response) {

		User aux = entityManager.find(User.class, id);
		List<Task> tasks = entityManager
				.createNamedQuery("Task.byUser", Task.class)
				.setParameter("user", aux)
				.getResultList();

		List<Task.Transfer> filterByUserList = new ArrayList<Task.Transfer>();

		for (Task t : tasks) {
			filterByUserList.add(t.toTransfer());
		}

		return filterByUserList;
	}

	// Filtro de tareas de una casa
	@GetMapping("/filterTasksHouse/{id}")
	@ResponseBody
	public List<Task.Transfer> filterTasksByHouse(@PathVariable long id, HttpServletResponse response) {
		List<Task> tasks = entityManager
				.createNamedQuery("Task.forHouse", Task.class)
				.setParameter("house", entityManager.find(House.class, id))
				.getResultList();

		List<Task.Transfer> filterByHouseList = new ArrayList<Task.Transfer>();

		for (Task t : tasks) {
			filterByHouseList.add(t.toTransfer());
		}

		return filterByHouseList;
	}

	// Información de una tarea
	@GetMapping("/getTaskInfo/{id}")
	@ResponseBody
	public Task.Transfer getInfoTask(@PathVariable long id, HttpServletResponse response) {
		Task target = entityManager.createNamedQuery("Task.byId", Task.class)
				.setParameter("taskId", id)
				.getSingleResult();

		return target.toTransfer();
	}

	/**
	 * TODO
	 * Returns JSON with all received USER messages
	 */
	// Notificaciones sin leer
	@GetMapping("/unReadNotifications")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Notification.Transfer> retrieveUserMessages(HttpSession session) {

		List<Notification> notifications = entityManager
				.createNamedQuery("Notification.userNotifications", Notification.class)
				.setParameter("userId", ((User) session.getAttribute("u")).getId())
				.getResultList();

		return notifications.stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}

	// ----- WebSockets -----
	/**
	 * TODO
	 * Returns JSON with count of unread messages
	 */
	@GetMapping(path = "unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		User u = (User) session.getAttribute("u");
		long unread = (long) entityManager.createNamedQuery("Notification.unRead")
				.setParameter("userId", u.getId())
				.getSingleResult();

		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}

	// ----- POSTs -----
	// Marcar notificaciones como leidas
	@PostMapping("/notificationRead")
	@Transactional
	@ResponseBody
	public Notification.Transfer delete(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) {

		Notification target = entityManager.find(Notification.class, data.get("notifId").asLong());
		target.setRead(true);
		entityManager.persist(target);
		entityManager.flush();

		return target.toTransfer();
	}

	// Crear una nueva tarea
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
		User u_session = (User) session.getAttribute("u");
		User u_task = entityManager.find(User.class, user_id);

		Task target = new Task();
		target.setTitle(title);
		target.setAuthor(u_session.getUsername());
		target.setRoom(entityManager.find(Room.class, room_id));
		target.setUser(u_task);
		target.setEnabled(true);

		target.setCreationDate(currentDate());

		entityManager.persist(target);
		entityManager.flush(); // forces DB to add user & assign valid id

		// Crear notification
		String msg = u_task.getUsername() + ", " + u_session.getUsername() + " te ha asignado a la tarea <u>" + title
				+ "</u>.";
		sendNotification("/topic/" + u_session.getHouse().getId(), u_task, msg);

		// Crear histórico
		String message = target.getAuthor() + " ha creado la tarea \"" + target.getTitle() + "\" en la habitación "
				+ target.getRoom().getName() + " para " + target.getUser().getUsername() + " el " + currentDate();
		createHistorical(message, "TASK", u_session.getHouse());

		return target.toTransfer();
	}

	// Modificar tarea
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

		// Notification
		User u_session = (User) session.getAttribute("u");
		String msg = "La tarea \"" + target.getTitle() + "\" ha sido modificada.";
		sendNotification("/topic/" + u_session.getHouse().getId(), target.getUser(), msg);

		// Crear histórico
		String message = target.getAuthor() + " ha modificado la tarea \"" + target.getTitle() + "\" el "
				+ currentDate();
		createHistorical(message, "TASK", u_session.getHouse());

		return target.toTransfer();
	}

	// Borrar tarea
	@PostMapping("/deleteTask")
	@Transactional
	@ResponseBody
	public Task.Transfer deleteTask(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) {

		long task_id = data.get("id").asLong();
		Task target = new Task();
		target = entityManager.find(Task.class, task_id);

		target.setEnabled(false);
		entityManager.persist(target);
		entityManager.flush();

		// Notification
		User u_session = (User) session.getAttribute("u");
		String msg = "La tarea \"" + target.getTitle() + "\" ha sido eliminada.";
		sendNotification("/topic/" + u_session.getHouse().getId(), target.getUser(), msg);

		// Crear histórico
		String message = target.getAuthor() + " ha borrado la tarea \"" + target.getTitle() + "\" el " + currentDate();
		createHistorical(message, "TASK", u_session.getHouse());

		return target.toTransfer();
	}

	// Crear una nueva casa
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

	// Unirse a una casa
	@PostMapping("/joinHouse") // TODO añadir al websocket de la casa
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

		// Notification
		String msg = user.getUsername() + " se ha unido a la casa.";
		for (User houseUser : h.getUsers()) {
			sendNotification("/topic/" + h.getId(), houseUser, msg);
		}

		return "redirect:/user/home";
	}

	// Crear una nueva habitación
	@PostMapping("/newRoom")
	@Transactional
	@ResponseBody
	public Room.Transfer newRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		User u = (User) session.getAttribute("u");

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return null;
		}

		Room roomNew = new Room();

		User user = entityManager.createNamedQuery("User.byUsername", User.class)
				.setParameter("username", u.getUsername())
				.getSingleResult();
		House h = user.getHouse();

		String roomName = data.get("roomName").asText();
		String roomPhoto = data.get("roomPhoto").asText();

		roomNew.setName(roomName);
		roomNew.setImg(roomPhoto);
		roomNew.setHouse(h);
		roomNew.setEnabled(true);

		entityManager.persist(roomNew);
		entityManager.flush();

		return roomNew.toTransfer();
	}

	// Modificar habitación
	@PostMapping("/updateRoom")
	@Transactional
	@ResponseBody
	public Room.Transfer updateRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		User u = (User) session.getAttribute("u");

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return null;
		}

		String roomName = data.get("name").asText(); // Obtén el nuevo nombre de la habitación
		long roomId = data.get("id").asLong(); // Obtén el ID de la habitación
		Room roomToUpdate = entityManager.find(Room.class, roomId); // Encuentra la habitación en la base de datos
		roomToUpdate.setName(roomName); // Actualiza el nombre de la habitación

		entityManager.persist(roomToUpdate); // Persiste los cambios en la base de datos
		entityManager.flush();

		return roomToUpdate.toTransfer(); // Devuelve los datos actualizados de la habitación
	}

	// Borrar habitación
	@PostMapping("/deleteRoom")
	@Transactional
	@ResponseBody
	public boolean deleteRoom(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		User u = (User) session.getAttribute("u");

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return false;
		}

		// Obtén el nuevo nombre de la habitación
		long roomId = data.get("id").asLong(); // Obtén el ID de la habitación
		List<Task> tasks = entityManager.createNamedQuery("Task.byRoom", Task.class)
				.setParameter("roomId", roomId).getResultList();

		if (tasks.size() > 0) {
			return false;
		} else {
			Room roomToDelete = entityManager.find(Room.class, roomId); // Encuentra la habitación en la base de datos
			roomToDelete.setEnabled(false);
			entityManager.persist(roomToDelete); // Persiste los cambios en la base de datos
			entityManager.flush();

			House houseUpdate = entityManager.find(House.class, roomToDelete.getHouse().getId());
			houseUpdate.setRooms(entityManager
					.createNamedQuery("Room.byHouse", Room.class)
					.setParameter("houseId", houseUpdate.getId())
					.getResultList());
			entityManager.persist(houseUpdate); // Persiste los cambios en la base de datos
			entityManager.flush();

			return true;
		}
	}

	// Expulsar usuario de una casa
	@PostMapping("/deleteUser") // TODO desvincular al usuario del websocket
	@Transactional
	@ResponseBody
	public boolean deleteUser(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		User u = (User) session.getAttribute("u");

		// En caso de no ser manager
		if (!u.hasRole(Role.MANAGER)) {
			return false;
		}

		// Obtén el nuevo nombre de la habitación
		long userId = data.get("id").asLong(); // Obtén el ID del usuario
		long newManagerId = data.get("newManager").asLong(); // Obtén el ID del nuevo manager
		User userToDelete = entityManager.find(User.class, userId); // Encuentra el usuario en la base de datos
		House h = entityManager.find(House.class, userToDelete.getHouse().getId());

		List<Task> tasks = entityManager.createNamedQuery("Task.byUser", Task.class)
				.setParameter("user", userToDelete).getResultList();

		String msg = "";
		if (tasks.size() > 0) {
			return false;
		} else if (userToDelete.getId() == ((User) session.getAttribute("u")).getId() && newManagerId == -1) {
			return false;
		} else {
			if (newManagerId == -1) {
				userToDelete.setHouse(null); // Desvincula al usuario de la casa
				entityManager.persist(userToDelete);
				entityManager.flush();

				// Notification
				msg = userToDelete.getUsername() + " ya no pertenece a la casa.";
			} else {
				userToDelete.setHouse(null); // Desvincula al usuario de la casa
				userToDelete.setRoles(Role.USER.name());
				entityManager.persist(userToDelete);

				session.setAttribute("u", userToDelete);

				User newManager = entityManager.find(User.class, newManagerId);
				newManager.setRoles(Role.MANAGER.name());
				entityManager.persist(newManager);

				entityManager.flush();

				// Notification cambio de manager
				msg = userToDelete.getUsername() + " ya no pertenece a la casa y el nuevo manager es "
						+ newManager.getUsername() + ".";
			}

			// Send notification to all house users

			for (User houseUser : h.getUsers()) {
				sendNotification("/topic/" + h.getId(), houseUser, msg);
			}

			return true;
		}
	}

	// Crear una nueva nota
	@PostMapping("/newNote")
	@Transactional
	@ResponseBody
	public Note.Transfer newNote(
			HttpServletResponse response,
			@RequestBody JsonNode data,
			Model model, HttpSession session) throws IOException {

		Task t = entityManager.find(Task.class, data.get("idTask").asLong());
		Note newNote = new Note();
		newNote.setAuthor(data.get("author").asText());
		newNote.setEnabled(true);
		newNote.setMessage(data.get("message").asText());
		newNote.setTask(t);

		entityManager.persist(newNote);
		entityManager.flush();

		// Para notificaciones
		User s_user = (User) session.getAttribute("u");
		String msg = data.get("author").asText() + " ha comentado en tu tarea \"<i>" + t.getTitle() + "</i>\".";
		sendNotification("/topic/" + s_user.getHouse().getId(), t.getUser(), msg);

		return newNote.toTransfer();
	}

	// Crear un nuevo gasto
	@PostMapping("/newExpense")
	@Transactional
	@ResponseBody
	public Expense.Transfer newExpense(HttpServletResponse response,
			@RequestBody JsonNode data, Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");

		// En caso de no tener casa asignada
		if (u.getHouse() == null) {
			return null;
		}

		String description = data.get("description").asText();
		Double quantity = data.get("quantity").asDouble();

		// Crear el gastos
		Expense newExpense = new Expense();
		newExpense.setAuthor(u);
		newExpense.setTitle(description);
		newExpense.setQuantity(quantity);
		newExpense.setDate(currentDate());
		newExpense.setHouse(u.getHouse());
		newExpense.setEnabled(true);
		entityManager.persist(newExpense);
		entityManager.flush();

		// TODO Ajustar la tabla UserExpense

		// Crear histórico
		String message = u.getUsername() + " ha creado el gasto con el concepto \"" + newExpense.getTitle()
				+ "\" por el valor de " + newExpense.getQuantity() + " el " + currentDate();
		createHistorical(message, "EXPENSE", u.getHouse());

		return newExpense.toTransfer();
	}

	// ----- UTILS -----
	// Mandar notificaciones
	public void sendNotification(String endpoint, User u_task, String msg) {

		// Crear objeto de la noti en la BD
		Notification notif = new Notification();
		notif.setDate(currentDate());
		notif.setEnabled(true);
		notif.setUser(u_task);
		notif.setMessage(msg);

		entityManager.persist(notif);
		entityManager.flush();

		// Mandar notificación
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonNotif = mapper.writeValueAsString(notif.toTransfer());
			log.info("Sending a notification to {} with contents '{}'", endpoint, jsonNotif);

			String json = "{\"type\" : \"NOTIFICATION\", \"notification\" : " + jsonNotif + "}";

			messagingTemplate.convertAndSend(endpoint, json);
		} catch (JsonProcessingException exception) {
			log.error("Failed to parse notification - {}}", notif);
			log.error("Exception {}", exception);
		}
	}

	public void createHistorical(String message, String type, House house) {
		// Crear histórico
		Historical history = new Historical();
		history.setType(type);
		history.setMessage(message);
		history.setHouse(house);

		entityManager.persist(history);
		entityManager.flush();
	}

	public Date currentDate() {
		long ms = System.currentTimeMillis();
		return new Date(ms);
	}
}