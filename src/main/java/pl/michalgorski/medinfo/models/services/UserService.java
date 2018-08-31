package pl.michalgorski.medinfo.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.michalgorski.medinfo.models.UserEntity;
import pl.michalgorski.medinfo.models.forms.UserForm;
import pl.michalgorski.medinfo.models.repositories.UserRepository;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

@Service
public class UserService {

    final SessionService sessionService;
    final UserRepository userRepository;

    @Autowired
    public UserService(SessionService sessionService, UserRepository userRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }


    public boolean tryToLogin(String username, String password) {
        Optional<UserEntity> userEntity = userRepository.findByUsernameAndPassword(username, password);
        if(userEntity.isPresent()) {
            sessionService.setLogin(true);
            sessionService.setUserEntity(userEntity.get());
        }
        return userEntity.isPresent();
    }

    public boolean tryToRegister(UserForm userForm) {
        if(userRepository.existsByUsername(userForm.getUsername())) {
            return false;
        }

        UserEntity userEntity = createEntityFromForm(userForm);
        userRepository.save(userEntity);
        return true;
    }

    private UserEntity createEntityFromForm(UserForm userForm) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(userForm.getUsername());
        userEntity.setPassword(userForm.getPassword());
        userEntity.setEmail(userForm.getEmail());
        userEntity.setAge(userForm.getAge());
        return userEntity;
    }


}
