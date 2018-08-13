package net.vincentkitowski.realize.controllers;

import net.vincentkitowski.realize.models.Post;
import net.vincentkitowski.realize.models.Volunteer;
import net.vincentkitowski.realize.repositories.PostRepository;
import net.vincentkitowski.realize.repositories.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(value = "/posts")
public class PostController extends AbstractBaseController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    VolunteerRepository volunteerRepository;

    @GetMapping
    public String listEvents(Model model) {
        List<Post> allPosts = postRepository.findAll();
        model.addAttribute("posts", allPosts);
        return "posts/list";
    }

    @GetMapping(value = "create")
    public String displayCreateEventForm(Model model, HttpServletRequest request) {
        model.addAttribute(new Post());
        model.addAttribute("actionUrl", request.getRequestURI());
        model.addAttribute("title", "Create Post");
        model.addAttribute("volunteers", volunteerRepository.findAll());
        return "posts/create-or-update";
    }

    @PostMapping(value = "create")
    public String processCreateEventForm(@Valid @ModelAttribute Post post,
                                         Errors errors,
                                         @RequestParam(name = "volunteers", required = false) List<Integer> volunteerUids) {

        if (errors.hasErrors())
            return "posts/create-or-update";

        syncVolunteerLists(volunteerUids, post.getVolunteers());
        postRepository.save(post);

        return "redirect:/posts/detail/" + post.getUid();
    }

    @GetMapping(value = "detail/{uid}")
    public String displayEventDetails(@PathVariable int uid, Model model) {

        model.addAttribute("title", "Post Details");

        Optional<Post> result = postRepository.findById(uid);
        if (result.isPresent()) {
            Post post = result.get();
            model.addAttribute(post);
            model.addAttribute("volunteerNames", post.getVolunteersFormatted());
        } else {
            model.addAttribute(MESSAGE_KEY, "warning|No event found with id: " + Integer.toString(uid));
        }

        return "posts/details";
    }

    @GetMapping(value = "update/{uid}")
    public String displayUpdateEventForm(@PathVariable int uid, Model model, HttpServletRequest request) {

        model.addAttribute("title", "Update Post");
        model.addAttribute("actionUrl", request.getRequestURI());

        Optional<Post> event = postRepository.findById(uid);
        if (event.isPresent()) {
            model.addAttribute(event.get());
            model.addAttribute("volunteers", volunteerRepository.findAll());
        } else {
            model.addAttribute(MESSAGE_KEY, "warning|No event found with id: " + Integer.toString(uid));
        }

        return "posts/create-or-update";
    }

    @PostMapping(value = "update/{uid}")
    public String processUpdateEventForm(@Valid @ModelAttribute Post post,
                                         RedirectAttributes model,
                                         Errors errors,
                                         @RequestParam(name = "volunteers", required = false) List<Integer> volunteerUids) {

        if (errors.hasErrors())
            return "posts/create-or-update";

        syncVolunteerLists(volunteerUids, post.getVolunteers());
        postRepository.save(post);
        model.addFlashAttribute(MESSAGE_KEY, "success|Updated Intention: " + post.getTitle());

        return "redirect:/posts/detail/" + post.getUid();
    }

    @PostMapping(value = "delete/{uid}")
    public String processDeleteEventForm(@PathVariable int uid, RedirectAttributes model) {

        Optional<Post> result = postRepository.findById(uid);
        if (result.isPresent()) {
            postRepository.delete(result.get());
            model.addFlashAttribute(MESSAGE_KEY, "success|Post deleted");
            return "redirect:/posts";
        } else {
            model.addFlashAttribute(MESSAGE_KEY, "danger|Post with ID does not exist: " +  uid);
            return "redirect:/posts";
        }
    }

    @PostMapping(value = "focus/{uid}")
    public String processFocusIntentForm(@PathVariable int uid, RedirectAttributes model) {

            return "redirect:/posts";
    }

    private void syncVolunteerLists(List<Integer> volunteerUids, List<Volunteer> volunteers) {

        if (volunteerUids == null)
            return;

        List<Volunteer> newVolunteerList = volunteerRepository.findAllById(volunteerUids);
        volunteers.removeIf(v -> volunteerUids.contains(v.getUid()));
        volunteers.addAll(newVolunteerList);
    }

}
