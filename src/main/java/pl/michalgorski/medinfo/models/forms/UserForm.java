package pl.michalgorski.medinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserForm {

    @Pattern(regexp = "[A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń0-9]{3,15}(\\.|_)?[A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń0-9]{0,15}")
    private String username;

    @Pattern(regexp = "[A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń0-9]{6,15}")
    private String password;

    @Pattern(regexp = "\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")
    private String email;

    @Pattern(regexp = "1?[0-9]{1,2}")
    private String age;
}
