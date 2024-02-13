package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/home1")
    public String TM_home1(Model model) {
        return "TM_home1";
    }

    @GetMapping("/home2")
    public String TM_home2(Model model) {
        return "TM_home2";
    }

    @GetMapping("/task")
    public String TM_tareas(Model model) {
        return "TM_task";
    }

    @GetMapping("/manager")
    public String TM_jefe(Model model) {
        return "TM_manager";
    }

    @GetMapping("/expenses")
    public String TM_gastos(Model model) {
        return "TM_expenses";
    }

    @GetMapping("/admin")
    public String TM_admin(Model model) {
        return "TM_admin";
    }
}
