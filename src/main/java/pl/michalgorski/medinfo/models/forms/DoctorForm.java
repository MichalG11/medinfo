package pl.michalgorski.medinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class DoctorForm {

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{3,20}(-| )?){0,3}")
    private String city;

    @Pattern(regexp = "([a-zęóąśłżźćń]{3,20}(-| )?){0,2}")
    private String specialization;

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{2,20})?")
    private String name;

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{2,20}(-| )?){0,3}")
    private String surname;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{3}")
    private String phoneNumber;

}
