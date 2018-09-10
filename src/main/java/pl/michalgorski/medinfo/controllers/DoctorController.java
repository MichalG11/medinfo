package pl.michalgorski.medinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.michalgorski.medinfo.models.DoctorEntity;
import pl.michalgorski.medinfo.models.forms.CommentForm;
import pl.michalgorski.medinfo.models.forms.DoctorForm;
import pl.michalgorski.medinfo.models.services.DoctorService;
import pl.michalgorski.medinfo.models.services.SessionService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DoctorController {

    @Value("${admin.id}")
    int adminId;

    final SessionService sessionService;
    final DoctorService doctorService;

    @Autowired
    public DoctorController(SessionService sessionService, DoctorService doctorService) {
        this.sessionService = sessionService;
        this.doctorService = doctorService;
    }

    @PostMapping("/findByCityAndSpecialization")
    public String findByCityAndSpecialization(@ModelAttribute("doctorForm") @Valid DoctorForm doctorForm,
                                              BindingResult bindingResult, Model model) {
        model.addAttribute("userObject", sessionService);
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            return "index";
        }
        if(doctorForm.getCity().equals("")) {
            model.addAttribute("doctors", doctorService.findDoctorBySpecialization(doctorForm.getSpecialization()));
        } else if (doctorForm.getSpecialization().equals("")) {
            model.addAttribute("doctors", doctorService.findDoctorByCity(doctorForm.getCity()));
        } else {
            model.addAttribute("doctors", doctorService.findDoctorByCityAndSpecialization(doctorForm.getCity(),
                                        doctorForm.getSpecialization()));
        }
        return "find";
    }

    @PostMapping("/findByNameAndSurname")
    public String findByNameAndSurname(@ModelAttribute("doctorForm") @Valid DoctorForm doctorForm,
                                       BindingResult bindingResult, Model model) {
        model.addAttribute("userObject", sessionService);
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors2", "Niepoprawne dane!");
            return "index";
        }
        if(doctorForm.getName().equals("")) {
            model.addAttribute("doctors", doctorService.findDoctorBySurname(doctorForm.getSurname()));
        } else if (doctorForm.getSurname().equals("")) {
            model.addAttribute("doctors", doctorService.findDoctorByName(doctorForm.getName()));
        } else {
            model.addAttribute("doctors", doctorService.findDoctorByNameAndSurname(doctorForm.getName(),
                    doctorForm.getSurname()));
        }
        return "find";
    }

    @GetMapping("/doctor/{id}")
    public String showDoctor(@PathVariable("id") int doctorId, Model model) {
        doctorService.saveAverageRatingForDoctorId(doctorId);
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        model.addAttribute("commentForm", new CommentForm());
        return "showDoctor";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int doctorId) {
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == adminId)) {
            return "redirect:/";
        }
        doctorService.deleteDoctor(doctorId);
        return "redirect:/";
    }

    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == adminId)) {
            return "redirect:/";
        }
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctorForm", new DoctorForm());
        return "addDoctor";
    }

    @PostMapping("/addDoctor")
    public String addDoctor(@ModelAttribute("doctorForm") @Valid DoctorForm doctorForm, BindingResult bindingResult,
                            Model model) {
        model.addAttribute("userObject", sessionService);
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == adminId)) {
            return "redirect:/";
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            return "addDoctor";
        }
        if(doctorService.isDoctorFormEmpty(doctorForm)) {
            model.addAttribute("infoAboutEmptyForm", "Uzupełnij pola!");
            return "addDoctor";
        }
        if(!doctorService.tryToAddDoctor(doctorForm)) {
            model.addAttribute("infoAboutAddDoctor", "Taki lekarz już istnieje w bazie!");
            return "addDoctor";
        }
        return "redirect:/";
    }

    @GetMapping("/change/{id}")
    public String change(@PathVariable("id") int doctorId, Model model) {
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == adminId)) {
            return "redirect:/";
        }
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        model.addAttribute("doctorForm", new DoctorForm());
        return "changeDoctor";
    }

    @PostMapping("/change/{id}")
    public String change(@PathVariable("id") int doctorId, Model model,
                         @ModelAttribute("doctorForm") @Valid DoctorForm doctorForm, BindingResult bindingResult) {
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == adminId)) {
            return "redirect:/";
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            return "changeDoctor";
        }
        if(doctorService.isDoctorFormEmpty(doctorForm)) {
            model.addAttribute("infoAboutEmptyForm", "Uzupełnij pola!");
            return "changeDoctor";
        }
        doctorService.changeDoctor(doctorId, doctorForm);
        return "redirect:/doctor/" + doctorId;
    }

    @GetMapping("allDoctorsByRating")
    public String allDoctors(Model model) {
        model.addAttribute("infoAboutSearch", "Sortowanie: ocena");
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctors", doctorService.showAllDoctorsByRating());
        return "find";
    }

    @GetMapping("allDoctorsByComments")
    public String allDoctorByComments(Model model) {
        model.addAttribute("infoAboutSearch", "Sortowanie: liczba opinii");
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctors", doctorService.showAllDoctorsByComments());
        return "find";
    }

    @GetMapping("allDoctorsByCity")
    public String allDoctorsByCity(Model model) {
        model.addAttribute("infoAboutSearch", "Sortowanie: miasto");
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctors", doctorService.showAllDoctorsByCity());
        return "find";
    }

    @GetMapping("allDoctorsBySpecialization")
    public String allDoctorsBySpecialization(Model model) {
        model.addAttribute("infoAboutSearch", "Sortowanie: specjalizacja");
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctors", doctorService.showAllDoctorsBySpecialization());
        return "find";
    }

    @GetMapping("allDoctorsBySurname")
    public String allDoctorsBySurname(Model model) {
        model.addAttribute("infoAboutSearch", "Sortowanie: nazwisko");
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctors", doctorService.showAllDoctorsBySurname());
        return "find";
    }

    @GetMapping("/{description}")
    public String showDoctorByLetters(@PathVariable("description") String description, Model model) {
        model.addAttribute("userObject", sessionService);
        List<String> words = Arrays.asList(description.split(" "));
        if(words.size() != 4) {
            return "redirect:/";
        }

        Optional<DoctorEntity> doctorEntity = doctorService.findDoctorByLetters(words.get(0), words.get(1), words.get(2),
                words.get(3));
        if(!doctorEntity.isPresent()) {
            return "redirect:/";
        }

        int doctorId = doctorEntity.get().getId();

        doctorService.saveAverageRatingForDoctorId(doctorId);
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        model.addAttribute("commentForm", new CommentForm());
        return "showDoctor";
    }

}
