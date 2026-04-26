package java_prac.controller;

import jakarta.validation.Valid;
import java_prac.model.Teacher;
import java_prac.service.CompanyService;
import java_prac.service.TeacherService;
import java_prac.web.form.TeacherForm;
import java_prac.web.view.TeacherDetailsView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final CompanyService companyService;

    public TeacherController(TeacherService teacherService, CompanyService companyService) {
        this.teacherService = teacherService;
        this.companyService = companyService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        TeacherDetailsView details = teacherService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Преподаватель не найден");
            return "error";
        }

        model.addAttribute("details", details);
        return "teachers/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("teacherForm", new TeacherForm());
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("formMode", "create");
        return "teachers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("teacherForm") TeacherForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "create");
            return "teachers/form";
        }

        try {
            Teacher created = teacherService.create(form);
            return "redirect:/teachers/" + created.getId();
        } catch (Exception ex) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "create");
            model.addAttribute("saveError", "Не удалось сохранить преподавателя");
            return "teachers/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TeacherDetailsView details = teacherService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Преподаватель не найден");
            return "error";
        }

        TeacherForm form = new TeacherForm();
        form.setFullName(details.getTeacher().getFullName());
        form.setCompanyId(details.getTeacher().getCompany().getId());

        model.addAttribute("teacherForm", form);
        model.addAttribute("teacherId", id);
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("formMode", "edit");
        return "teachers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("teacherForm") TeacherForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teacherId", id);
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "edit");
            return "teachers/form";
        }

        try {
            Teacher updated = teacherService.update(id, form).orElse(null);
            if (updated == null) {
                model.addAttribute("errorMessage", "Преподаватель не найден");
                return "error";
            }

            return "redirect:/teachers/" + updated.getId();
        } catch (Exception ex) {
            model.addAttribute("teacherId", id);
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("formMode", "edit");
            model.addAttribute("saveError", "Не удалось обновить преподавателя");
            return "teachers/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            boolean deleted = teacherService.deleteById(id);
            if (!deleted) {
                model.addAttribute("errorMessage", "Преподаватель не найден");
                return "error";
            }
            return "redirect:/teachers";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Не удалось удалить преподавателя");
            return "error";
        }
    }
}