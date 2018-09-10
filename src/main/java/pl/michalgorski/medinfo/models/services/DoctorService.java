package pl.michalgorski.medinfo.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.michalgorski.medinfo.models.CommentEntity;
import pl.michalgorski.medinfo.models.DoctorEntity;
import pl.michalgorski.medinfo.models.forms.DoctorForm;
import pl.michalgorski.medinfo.models.repositories.DoctorRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    final SessionService sessionService;
    final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(SessionService sessionService, DoctorRepository doctorRepository) {
        this.sessionService = sessionService;
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorEntity> findDoctorByCityAndSpecialization(String city, String specialization) {
        return doctorRepository.findByCity(city).stream()
                .filter(s -> s.getSpecialization().equals(specialization))
                .sorted((s, s1) -> Double.compare(s.getAverageRating(), s1.getAverageRating()) * -1)
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> findDoctorBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .sorted((s, s1) -> Double.compare(s.getAverageRating(), s1.getAverageRating()) * -1)
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> findDoctorByCity(String city) {
        return doctorRepository.findByCity(city).stream()
                .sorted((s, s1) -> Double.compare(s.getAverageRating(), s1.getAverageRating()) * -1)
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> findDoctorByName(String name) {
        return doctorRepository.findByName(name).stream()
                .sorted((s, s1) -> s.getSurname().compareTo(s1.getSurname()))
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> findDoctorBySurname(String surname) {
        return doctorRepository.findBySurname(surname).stream()
                .sorted((s, s1) -> s.getName().compareTo(s1.getName()))
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> findDoctorByNameAndSurname(String name, String surname) {
        return doctorRepository.findByName(name).stream()
                .filter(s -> s.getSurname().equals(surname))
                .collect(Collectors.toList());
    }

    public DoctorEntity getAllDataAboutDoctor(int doctorId) {
        return doctorRepository.findById(doctorId).get();
    }

    public void deleteDoctor(int doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    public List<DoctorEntity> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public boolean tryToAddDoctor(DoctorForm doctorForm) {
        if(getAllDoctors().stream()
                .filter(s -> s.getName().equals(doctorForm.getName()))
                .filter(s -> s.getSurname().equals(doctorForm.getSurname()))
                .filter(s -> s.getCity().equals(doctorForm.getCity()))
                .filter(s -> s.getSpecialization().equals(doctorForm.getSpecialization()))
                .collect(Collectors.toList()).size() != 0) {
            return false;
        }
        DoctorEntity doctorEntity = createNewDoctorEntityFromForm(doctorForm);
        doctorRepository.save(doctorEntity);
        return true;
    }

    public boolean isDoctorFormEmpty(DoctorForm doctorForm) {
        if(doctorForm.getName().equals("") || doctorForm.getSurname().equals("") ||
                doctorForm.getCity().equals("") || doctorForm.getSpecialization().equals("")) {
            return true;
        }
        return false;
    }

    private DoctorEntity createNewDoctorEntityFromForm(DoctorForm doctorForm) {
        DoctorEntity doctorEntity = new DoctorEntity();

        fillDoctorEntity(doctorForm, doctorEntity);
        return doctorEntity;
    }

    private void fillDoctorEntity(DoctorForm doctorForm, DoctorEntity doctorEntity) {
        doctorEntity.setName(doctorForm.getName());
        doctorEntity.setSurname(doctorForm.getSurname());
        doctorEntity.setCity(doctorForm.getCity());
        doctorEntity.setSpecialization(doctorForm.getSpecialization());
        doctorEntity.setPhoneNumber(doctorForm.getPhoneNumber());
    }

    public void changeDoctor(int doctorId, DoctorForm doctorForm) {
        DoctorEntity doctorEntity = getAllDataAboutDoctor(doctorId);

        fillDoctorEntity(doctorForm, doctorEntity);
        doctorRepository.save(doctorEntity);
    }

    public void increaseQuantityOfComments(int doctorId) {
        DoctorEntity doctorEntity = getAllDataAboutDoctor(doctorId);

        doctorEntity.setQuantityOfComments(doctorEntity.getQuantityOfComments() + 1);
        doctorRepository.save(doctorEntity);
    }

    public void decreaseQuantityOfComments(int doctorId) {
        DoctorEntity doctorEntity = getAllDataAboutDoctor(doctorId);

        doctorEntity.setQuantityOfComments(doctorEntity.getQuantityOfComments() - 1);
        doctorRepository.save(doctorEntity);
    }

    public void saveAverageRatingForDoctorId(int doctorId) {
        DoctorEntity doctorEntity = getAllDataAboutDoctor(doctorId);

        doctorEntity.setAverageRating(calculateAverageRatingForDoctorId(doctorId));
        doctorRepository.save(doctorEntity);
    }

    public double calculateAverageRatingForDoctorId(int doctorId) {
        DoctorEntity doctorEntity = getAllDataAboutDoctor(doctorId);

        double averageRating = doctorEntity.getComments().stream()
                .collect(Collectors.averagingDouble(s -> (Integer.parseInt(s.getRating()))));

        averageRating = Math.round(averageRating * 10);
        averageRating = averageRating / 10;
        return averageRating;
    }

    public List<DoctorEntity> showAllDoctorsByRating() {
        return doctorRepository.findAll().stream()
                .sorted((s, s1) -> Double.compare(s.getAverageRating(), s1.getAverageRating()) * -1)
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> showAllDoctorsByComments() {
        return doctorRepository.findAll().stream()
                .sorted((s, s1) -> Integer.compare(s.getQuantityOfComments(), s1.getQuantityOfComments()) * -1)
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> showAllDoctorsByCity() {
        return doctorRepository.findAll().stream()
                .sorted((s, s1) -> s.getCity().compareTo(s1.getCity()))
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> showAllDoctorsBySpecialization() {
        return doctorRepository.findAll().stream()
                .sorted((s, s1) -> s.getSpecialization().compareTo(s1.getSpecialization()))
                .collect(Collectors.toList());
    }

    public List<DoctorEntity> showAllDoctorsBySurname() {
        return doctorRepository.findAll().stream()
                .sorted((s, s1) -> s.getSurname().compareTo(s1.getSurname()))
                .collect(Collectors.toList());
    }

    @Bean
    public List<String> getActualDoctors() {
        return doctorRepository.findAll().stream()
                .map(s -> s.getCity() +" "+ s.getSpecialization() +" "+ s.getName() +" "+ s.getSurname())
                .collect(Collectors.toList());
    }

    public Optional<DoctorEntity> findDoctorByLetters(String city, String specialization, String name, String surname) {
        List<DoctorEntity> listDoctorsByLetters = doctorRepository.findByCity(city).stream()
                .filter(s -> s.getSpecialization().equals(specialization))
                .filter(s -> s.getName().equals(name))
                .filter(s -> s.getSurname().equals(surname))
                .collect(Collectors.toList());

        if(listDoctorsByLetters.size() == 0 || listDoctorsByLetters.size() > 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(listDoctorsByLetters.get(0));
    }


}
