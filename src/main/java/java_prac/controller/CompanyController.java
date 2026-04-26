package java_prac.controller;

import jakarta.validation.Valid;
import java_prac.model.Company;
import java_prac.service.CompanyService;
import java_prac.web.form.CompanyForm;
import java_prac.web.view.CompanyDetailsView;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("companies", companyService.findAll());
        return "companies/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        CompanyDetailsView details = companyService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Компания не найдена");
            return "error";
        }

        model.addAttribute("details", details);
        return "companies/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("companyForm", new CompanyForm());
        model.addAttribute("formMode", "create");
        return "companies/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("companyForm") CompanyForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formMode", "create");
            return "companies/form";
        }

        try {
            Company created = companyService.create(form);
            return "redirect:/companies/" + created.getId();
        } catch (Exception ex) {
            model.addAttribute("formMode", "create");
            model.addAttribute("saveError", "Не удалось сохранить компанию. Возможно, компания с таким названием уже существует.");
            return "companies/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CompanyDetailsView details = companyService.findDetailsById(id).orElse(null);
        if (details == null) {
            model.addAttribute("errorMessage", "Компания не найдена");
            return "error";
        }

        CompanyForm form = new CompanyForm();
        form.setName(details.getCompany().getName());
        form.setAddress(details.getCompany().getAddress());

        model.addAttribute("companyForm", form);
        model.addAttribute("companyId", id);
        model.addAttribute("formMode", "edit");
        return "companies/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("companyForm") CompanyForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companyId", id);
            model.addAttribute("formMode", "edit");
            return "companies/form";
        }

        try {
            Company updated = companyService.update(id, form).orElse(null);
            if (updated == null) {
                model.addAttribute("errorMessage", "Компания не найдена");
                return "error";
            }

            return "redirect:/companies/" + updated.getId();
        } catch (Exception ex) {
            model.addAttribute("companyId", id);
            model.addAttribute("formMode", "edit");
            model.addAttribute("saveError", "Не удалось обновить компанию. Возможно, компания с таким названием уже существует.");
            return "companies/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            boolean deleted = companyService.deleteById(id);
            if (!deleted) {
                model.addAttribute("errorMessage", "Компания не найдена");
                return "error";
            }
            return "redirect:/companies";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Не удалось удалить компанию. Возможно, с ней связаны преподаватели или курсы.");
            return "error";
        }
    }
}