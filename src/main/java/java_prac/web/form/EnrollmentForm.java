package java_prac.web.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentForm {

    @NotNull(message = "Нужно выбрать курс")
    private Long courseId;
}