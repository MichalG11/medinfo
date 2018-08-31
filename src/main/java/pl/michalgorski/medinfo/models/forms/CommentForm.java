package pl.michalgorski.medinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CommentForm {

    @Pattern(regexp = "([A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń0-9]{1,15}(\\.|_|,|, | |\\n|\\?|\\!)?){3,150}")
    private String context;

    @Pattern(regexp = "[1-5]")
    private String rating;

}
