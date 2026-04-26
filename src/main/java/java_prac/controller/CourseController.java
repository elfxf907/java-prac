package java_prac.controller;

import jakarta.validation.Valid;
import java_prac.model.Course;
import java_prac.service.CompanyService;
import java_prac.service.CourseService;
import java_prac.web.form.CourseForm;
import java_prac.web.view.CourseDetailsView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java_prac.service.LessonService;
import java_prac.service.TeacherService;
import java_prac.web.form.LessonForm;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CompanyService companyService;
    private final LessonService lessonService;
    private final TeacherService teacherService;

    public CourseController(CourseService courseService,
                            CompanyService companyService,
                            LessonService lessonService,
                            TeacherService teacherService) {
        this.courseService = courseService;
        this.companyService = companyService;
        this.lessonService = lessonService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "courses/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        CourseDetailsView details = courseService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Курс не найден");
            return "error";
        }

        model.addAttribute("details", details);
        return "courses/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("courseForm", new CourseForm());
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("formMode", "create");
        model.addAttribute("durationTypes", java_prac.model.CourseDurationType.values());
        return "courses/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("courseForm") CourseForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "create");
            return "courses/form";
        }

        try {
            Course created = courseService.create(form);
            return "redirect:/courses/" + created.getId();
        } catch (Exception ex) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "create");
            model.addAttribute("saveError", "Не удалось сохранить курс");
            return "courses/form";
        }
        
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CourseDetailsView details = courseService.findDetailsById(id).orElse(null);
        model.addAttribute("durationTypes", java_prac.model.CourseDurationType.values());
        if (details == null) {
            model.addAttribute("errorMessage", "Курс не найден");
            return "error";
        }

        CourseForm form = new CourseForm();
        form.setTitle(details.getCourse().getTitle());
        form.setDescription(details.getCourse().getDescription());
        form.setCompanyId(details.getCourse().getCompany().getId());
        form.setDurationType(details.getCourse().getDurationType());
        form.setHoursPerDay(details.getCourse().getHoursPerDay());


        model.addAttribute("courseForm", form);
        model.addAttribute("courseId", id);
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("formMode", "edit");
        return "courses/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("courseForm") CourseForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courseId", id);
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "edit");
            return "courses/form";
        }

        try {
            Course updated = courseService.update(id, form).orElse(null);
            if (updated == null) {
                model.addAttribute("errorMessage", "Курс не найден");
                return "error";
            }

            return "redirect:/courses/" + updated.getId();
        } catch (Exception ex) {
            model.addAttribute("courseId", id);
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "edit");
            model.addAttribute("saveError", "Не удалось обновить курс");
            return "courses/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            boolean deleted = courseService.deleteById(id);
            if (!deleted) {
                model.addAttribute("errorMessage", "Курс не найден");
                return "error";
            }
            return "redirect:/courses";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Не удалось удалить курс");
            return "error";
        }
    }
    @GetMapping("/{id}/lessons/new")
    public String newLessonForm(@PathVariable Long id, Model model) {
        CourseDetailsView details = courseService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Курс не найден");
            return "error";
        }

        model.addAttribute("course", details.getCourse());
        model.addAttribute("lessonForm", new LessonForm());
        model.addAttribute("teachers", teacherService.findAll());
        return "courses/lesson-form";
    }
    @PostMapping("/{id}/lessons")
    public String createLesson(@PathVariable Long id,
                               @Valid @ModelAttribute("lessonForm") LessonForm form,
                               BindingResult bindingResult,
                               Model model) {
        CourseDetailsView details = courseService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Курс не найден");
            return "error";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("course", details.getCourse());
            model.addAttribute("teachers", teacherService.findAll());
            return "courses/lesson-form";
        }

        if (form.getStartTime() != null && form.getEndTime() != null
                && !form.getEndTime().isAfter(form.getStartTime())) {
            model.addAttribute("course", details.getCourse());
            model.addAttribute("teachers", teacherService.findAll());
            model.addAttribute("saveError", "Время окончания должно быть позже времени начала");
            return "courses/lesson-form";
        }

        if (lessonService.createForCourse(id, form).isEmpty()) {
            model.addAttribute("course", details.getCourse());
            model.addAttribute("teachers", teacherService.findAll());
            model.addAttribute("saveError", "Не удалось создать занятие");
            return "courses/lesson-form";
        }

        return "redirect:/courses/" + id;
    }
}