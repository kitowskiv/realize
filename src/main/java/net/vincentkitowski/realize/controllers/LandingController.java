package net.vincentkitowski.realize.controllers;

import net.vincentkitowski.realize.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

import static net.vincentkitowski.realize.controllers.AbstractBaseController.MESSAGE_KEY;


@Controller
public class LandingController {
    @Autowired
    PostRepository postRepository;

    @GetMapping(value = "/landing")
    public String landing(Model model) {
        return "landing";

    }
}
