package java_prac.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentForm {

    @NotBlank(message = "ФИО не должно быть пустым")
    @Size(max = 255, message = "ФИО слишком длинное")
    private String fullName;
}