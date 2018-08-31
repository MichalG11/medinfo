package pl.michalgorski.medinfo.models.services;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.michalgorski.medinfo.models.UserEntity;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class SessionService {

    private boolean isLogin;
    private UserEntity userEntity;

}
