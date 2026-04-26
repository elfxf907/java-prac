package java_prac.web.view;

import java_prac.model.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleResultView {
    private String searchType;
    private String subjectName;
    private List<Lesson> lessons;
}