package net.vincentkitowski.realize.controllers;

import net.vincentkitowski.realize.models.Volunteer;
import net.vincentkitowski.realize.repositories.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "volunteers")
public class VolunteerController extends AbstractBaseController {

    @Autowired
    VolunteerRepository volunteerRepository;

    @GetMapping
    public String listVolunteers(Model model) {
        model.addAttribute("title", "Volunteers");
        model.addAttribute("volunteers", volunteerRepository.findAll());
        return "volunteers/list";
    }

    @GetMapping(value = "create")
    public String displayCreateVolunteerForm(Model model, HttpServletRequest request) {
        model.addAttribute("title", "Create Volunteer");
        model.addAttribute(new Volunteer());
        model.addAttribute("actionUrl", request.getRequestURI());
        return "volunteers/create-or-update.html";
    }

    @PostMapping(value = "create")
    public String processCreateVolunteerForm(@ModelAttribute @Valid Volunteer volunteer,
                                             Errors errors,
                                             RedirectAttributes model) {

        if (errors.hasErrors())
            return "volunteers/create-or-update";

        volunteerRepository.save(volunteer);
        model.addFlashAttribute(MESSAGE_KEY, "success|New volunteer added: " + volunteer.getFullName());

        return "redirect:/volunteers";
    }
}
