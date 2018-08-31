package pl.michalgorski.medinfo.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.michalgorski.medinfo.models.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

}
