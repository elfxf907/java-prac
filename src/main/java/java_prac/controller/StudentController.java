package java_prac.controller;

import jakarta.validation.Valid;
import java_prac.model.Student;
import java_prac.service.StudentService;
import java_prac.web.form.StudentForm;
import java_prac.web.view.StudentDetailsView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java_prac.web.form.EnrollmentForm;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        StudentDetailsView details = studentService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Обучающийся не найден");
            return "error";
        }
        model.addAttribute("details", details);
        model.addAttribute("enrollmentForm", new EnrollmentForm());
        return "students/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studentForm", new StudentForm());
        model.addAttribute("formMode", "create");
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formMode", "create");
            return "students/form";
        }

        Student created = studentService.create(form);
        return "redirect:/students/" + created.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        StudentDetailsView details = studentService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Обучающийся не найден");
            return "error";
        }

        StudentForm form = new StudentForm();
        form.setFullName(details.getStudent().getFullName());

        model.addAttribute("studentForm", form);
        model.addAttribute("studentId", id);
        model.addAttribute("formMode", "edit");
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("studentId", id);
            model.addAttribute("formMode", "edit");
            return "students/form";
        }

        Student updated = studentService.update(id, form).orElse(null);
        if (updated == null) {
            model.addAttribute("errorMessage", "Обучающийся не найден");
            return "error";
        }

        return "redirect:/students/" + updated.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        boolean deleted = studentService.deleteById(id);
        if (!deleted) {
            model.addAttribute("errorMessage", "Не удалось удалить обучающегося");
            return "error";
        }
        return "redirect:/students";
    }
    @PostMapping("/{id}/enroll")
    public String enroll(@PathVariable Long id,
                         @Valid @ModelAttribute("enrollmentForm") EnrollmentForm form,
                         BindingResult bindingResult,
                         Model model) {
        StudentDetailsView details = studentService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Обучающийся не найден");
            return "error";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("details", details);
            model.addAttribute("enrollmentForm", form);
            return "students/view";
        }

        try {
            boolean enrolled = studentService.enrollToCourse(id, form.getCourseId());
            if (!enrolled) {
                model.addAttribute("details", details);
                model.addAttribute("enrollmentForm", form);
                model.addAttribute("saveError", "Не удалось записать обучающегося на курс");
                return "students/view";
            }
        } catch (Exception ex) {
            model.addAttribute("details", details);
            model.addAttribute("enrollmentForm", form);
            model.addAttribute("saveError", "Ошибка при записи на курс");
            return "students/view";
        }

        return "redirect:/students/" + id;
    }
}