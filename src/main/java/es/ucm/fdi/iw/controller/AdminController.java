package es.ucm.fdi.iw.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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

import com.fasterxml.jackson.databind.JsonNode;

import es.ucm.fdi.iw.model.House;
import es.ucm.fdi.iw.model.Task;
import es.ucm.fdi.iw.model.User;

/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    private static final Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User u = (User) session.getAttribute("u");

        List<House> houses = entityManager
                .createNamedQuery("House.allActiveHouses", House.class)
                .setParameter("status", true)
                .getResultList();

        model.addAttribute("houses", houses);
        model.addAttribute("u", u);

        return "admin";
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
}
