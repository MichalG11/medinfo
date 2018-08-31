package pl.michalgorski.medinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.michalgorski.medinfo.models.forms.UserForm;
import pl.michalgorski.medinfo.models.services.SessionService;
import pl.michalgorski.medinfo.models.services.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    final SessionService sessionService;
    final UserService userService;

    @Autowired
    public UserController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userObject", sessionService);
        model.addAttribute("userForm", new UserForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                        Model model) {
        model.addAttribute("userObject", sessionService);
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            return "login";
        }
        if(!userService.tryToLogin(userForm.getUsername(), userForm.getPassword())) {
            model.addAttribute("infoAboutLogin", "Nie ma takiego użytkownika!");
            return "login";
        }
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userObject", sessionService);
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                           Model model) {
        model.addAttribute("userObject", sessionService);
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            return "register";
        }
        if(!userService.tryToRegister(userForm)) {
            model.addAttribute("infoAboutRegister", "Nazwa użytkownika jest już zajęta!");
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        sessionService.setUserEntity(null);
        sessionService.setLogin(false);
        return "redirect:/login";
    }


}
