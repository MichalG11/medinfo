package pl.michalgorski.medinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CommentForm {

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń0-9]{1,15}(\\.|_|,|, | |\\n|\\?|\\!)?){3,150}",
            message = "tekst, cyfry, może zawierać kropkę, przecinek, ?, ! spację, od 3 do 150 znaków")
    private String context;

    @Pattern(regexp = "[1-5]", message = "cyfra od 1 do 5")
    private String rating;

}
