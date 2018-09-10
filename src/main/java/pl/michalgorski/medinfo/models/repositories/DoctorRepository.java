package pl.michalgorski.medinfo.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.michalgorski.medinfo.models.DoctorEntity;

import java.security.cert.PKIXRevocationChecker;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {

    List<DoctorEntity> findByCity(String city);

    List<DoctorEntity> findBySpecialization(String specialization);

    List<DoctorEntity> findByName(String name);

    List<DoctorEntity> findBySurname(String surname);

    List<DoctorEntity> findAll();

}
