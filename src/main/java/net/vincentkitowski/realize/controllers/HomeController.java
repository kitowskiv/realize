package net.vincentkitowski.realize.controllers;

import net.vincentkitowski.realize.models.Post;
import net.vincentkitowski.realize.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<Post> allPosts = postRepository.findAll();
        model.addAttribute("posts", allPosts);
        return "posts/list";
    }
}
