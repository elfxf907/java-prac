package java_prac.web.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonForm {

    @NotNull(message = "Нужно выбрать преподавателя")
    private Long teacherId;

    @NotNull(message = "Нужно указать дату и время начала")
    private LocalDateTime startTime;

    @NotNull(message = "Нужно указать дату и время окончания")
    private LocalDateTime endTime;
}