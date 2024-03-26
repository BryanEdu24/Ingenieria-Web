package es.ucm.fdi.iw.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);
    
	@Autowired
	private EntityManager entityManager;

    @Autowired
	private PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

    @GetMapping("/login")
    public String login(Model model) {
        return "TM_login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "TM_login";
    }

    @GetMapping("/register")
	public String TM_register(Model model) {
		return "TM_register";
	}

	@PostMapping("/newuser")
	@Transactional
	public String register(
		HttpServletResponse response,
		@RequestParam String email,
		@RequestParam String username,
		@RequestParam String password,
		Model model, HttpSession session) throws IOException {
            
        try {
            User user = entityManager.createNamedQuery("User.byemail", User.class)
			.setParameter("useremail", email)
			.getSingleResult();
            return "redirect:/register";
        } catch (NoResultException ex) {
            User usernew = new User();
			usernew.setUsername(username);
			usernew.setEmail(email);
			usernew.setPassword(encodePassword(password));
			usernew.setRoles(Role.USER.name());
			usernew.setHouse(null);
			usernew.setEnabled(true);

			entityManager.persist(usernew);
			entityManager.flush();
        }

		return "redirect:/login";
	}
}
