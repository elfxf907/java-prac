package java_prac.service;

import java_prac.web.form.ScheduleSearchForm;
import java_prac.web.view.ScheduleResultView;

import java.util.Optional;

public interface ScheduleService {
    Optional<ScheduleResultView> search(ScheduleSearchForm form);
}