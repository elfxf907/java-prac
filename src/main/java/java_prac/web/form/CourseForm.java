package java_prac.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java_prac.model.CourseDurationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseForm {

    @NotBlank(message = "Название курса не должно быть пустым")
    @Size(max = 255, message = "Название курса слишком длинное")
    private String title;

    @Size(max = 2000, message = "Описание слишком длинное")
    private String description;

    @NotNull(message = "Нужно выбрать компанию")
    private Long companyId;

    @NotNull(message = "Нужно указать тип длительности")
    private CourseDurationType durationType;

    @NotNull(message = "Нужно указать часов в день")
    @Min(value = 1, message = "Часов в день должно быть не меньше 1")
    private Integer hoursPerDay;
}