package java_prac.web.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleSearchForm {

    @NotNull(message = "Нужно выбрать тип поиска")
    private String searchType;

    private Long studentId;

    private Long teacherId;

    @NotNull(message = "Нужно указать дату и время начала периода")
    private LocalDateTime from;

    @NotNull(message = "Нужно указать дату и время конца периода")
    private LocalDateTime to;
}