package pl.michalgorski.medinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.michalgorski.medinfo.models.DoctorEntity;
import pl.michalgorski.medinfo.models.forms.CommentForm;
import pl.michalgorski.medinfo.models.services.CommentService;
import pl.michalgorski.medinfo.models.services.DoctorService;
import pl.michalgorski.medinfo.models.services.SessionService;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class CommentController {

    final SessionService sessionService;
    final CommentService commentService;
    final DoctorService doctorService;

    @Autowired
    public CommentController(SessionService sessionService, CommentService commentService, DoctorService doctorService) {
        this.sessionService = sessionService;
        this.commentService = commentService;
        this.doctorService = doctorService;
    }


    @PostMapping("/comment/{id}")
    public String comment(@PathVariable("id") int doctorId, Model model,
                          @ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult) {
        if(!sessionService.isLogin()) {
            return "redirect:/login";
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors", "Niepoprawne dane!");
            model.addAttribute("userObject", sessionService);
            model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
            return "showDoctor";
        }
        commentService.addComment(commentForm, doctorId);
        return "redirect:/doctor/" + doctorId;
    }

    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable("id") int doctorId, Model model) {
        model.addAttribute("userObject", sessionService);
        if(!sessionService.isLogin()) {
            return "redirect:/login";
        }
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        return "deleteComment";
    }

    @GetMapping("/deleteCommentById/{doctorId}/{commentId}")
    public String deleteCommentById(@PathVariable("doctorId") int doctorId,
                                    @PathVariable("commentId") int commentId, Model model) {
        if(!(sessionService.isLogin() && sessionService.getUserEntity().getId() == 1)) {
            return "redirect:/index";
        }
        model.addAttribute("userObject", sessionService);
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        commentService.deleteCommentById(doctorId, commentId);
        return "redirect:/doctor/" + doctorId;
    }

    @GetMapping("/deleteMyCommentById/{doctorId}/{commentId}")
    public String deleteMyCommentById(@PathVariable("doctorId") int doctorId,
                                    @PathVariable("commentId") int commentId, Model model) {
        model.addAttribute("userObject", sessionService);
        if(sessionService.isLogin() &&
                (commentService.getCommentById(commentId).get().getUser().getId() ==
                        sessionService.getUserEntity().getId())) {
            commentService.deleteCommentById(doctorId, commentId);
            return "redirect:/doctor/" + doctorId;
        }
        model.addAttribute("doctorData", doctorService.getAllDataAboutDoctor(doctorId));
        model.addAttribute("infoAboutCommentDelete", "Nie możesz usuwać opinii innych użytkowników!");
        return "deleteComment";
    }


}
