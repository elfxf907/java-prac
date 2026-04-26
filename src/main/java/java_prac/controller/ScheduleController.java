package java_prac.controller;

import jakarta.validation.Valid;
import java_prac.service.ScheduleService;
import java_prac.service.StudentService;
import java_prac.service.TeacherService;
import java_prac.web.form.ScheduleSearchForm;
import java_prac.web.view.ScheduleResultView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public ScheduleController(ScheduleService scheduleService,
                              StudentService studentService,
                              TeacherService teacherService) {
        this.scheduleService = scheduleService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("scheduleSearchForm", new ScheduleSearchForm());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "schedule/form";
    }

    @PostMapping
    public String search(@Valid ScheduleSearchForm scheduleSearchForm,
                         BindingResult bindingResult,
                         Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("teachers", teacherService.findAll());

        if (bindingResult.hasErrors()) {
            return "schedule/form";
        }

        if ("student".equals(scheduleSearchForm.getSearchType()) && scheduleSearchForm.getStudentId() == null) {
            model.addAttribute("saveError", "Нужно выбрать обучающегося");
            return "schedule/form";
        }

        if ("teacher".equals(scheduleSearchForm.getSearchType()) && scheduleSearchForm.getTeacherId() == null) {
            model.addAttribute("saveError", "Нужно выбрать преподавателя");
            return "schedule/form";
        }

        if (scheduleSearchForm.getFrom() != null && scheduleSearchForm.getTo() != null
                && scheduleSearchForm.getTo().isBefore(scheduleSearchForm.getFrom())) {
            model.addAttribute("saveError", "Конец периода не может быть раньше начала");
            return "schedule/form";
        }

        ScheduleResultView result = scheduleService.search(scheduleSearchForm).orElse(null);
        if (result == null) {
            model.addAttribute("saveError", "Не удалось найти расписание");
            return "schedule/form";
        }

        model.addAttribute("result", result);
        model.addAttribute("from", scheduleSearchForm.getFrom());
        model.addAttribute("to", scheduleSearchForm.getTo());
        return "schedule/result";
    }
}