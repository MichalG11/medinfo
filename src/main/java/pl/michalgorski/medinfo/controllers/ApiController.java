package pl.michalgorski.medinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.michalgorski.medinfo.models.services.DoctorService;

import java.util.stream.Collectors;

@RestController
public class ApiController {

    final DoctorService doctorService;

    @Autowired
    public ApiController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctors/findDoctorByLetters/{letters}")
    public ResponseEntity doctorsByLetters(@PathVariable("letters") String letters) {
        return ResponseEntity
                .ok()
                .body(doctorService.getActualDoctors()
                        .stream()
                        .filter(s -> s.contains(letters))
                        .collect(Collectors.toList()));
    }

}
