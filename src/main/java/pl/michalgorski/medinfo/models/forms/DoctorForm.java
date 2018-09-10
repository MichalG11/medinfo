package pl.michalgorski.medinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class DoctorForm {

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{2,20}(-| )?){0,3}",
            message = "Pierwsza duża litera, może zawierać spację lub -")
    private String city;

    @Pattern(regexp = "([a-zęóąśłżźćń]{3,20}(-| )?){0,2}",
            message = "może zawierać spację lub -")
    private String specialization;

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{2,20})?", message = "Pierwsza duża litera, od 3 do 20 znaków")
    private String name;

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃ][a-zęóąśłżźćń]{2,20}(-| )?){0,3}",
            message = "Pierwsza duża litera, od 3 do 20 znaków, może zawierać spację lub -")
    private String surname;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{3}", message = "Format numeru: xxx-xxx-xxx")
    private String phoneNumber;

}
