package pl.michalgorski.medinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.michalgorski.medinfo.models.forms.DoctorForm;
import pl.michalgorski.medinfo.models.services.SessionService;

@Controller
public class MainController {

    final SessionService sessionService;

    @Autowired
    public MainController(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("doctorForm", new DoctorForm());
        model.addAttribute("userObject", sessionService);
        return "index";
    }


}
