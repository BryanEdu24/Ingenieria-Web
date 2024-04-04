package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.House;
import es.ucm.fdi.iw.model.Task;
import es.ucm.fdi.iw.model.User;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
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
			.createNamedQuery("House.allHouses", House.class)
			.setParameter("status", true)
			.getResultList();

        model.addAttribute("houses", houses);
        model.addAttribute("u", u);

        return "TM_admin";
    }
}
