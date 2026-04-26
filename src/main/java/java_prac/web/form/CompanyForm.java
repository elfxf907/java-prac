package java_prac.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyForm {

    @NotBlank(message = "Название компании не должно быть пустым")
    @Size(max = 255, message = "Название компании слишком длинное")
    private String name;

    @Size(max = 500, message = "Адрес слишком длинный")
    private String address;
}