package com.yehor.helper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yehor.helper.models.Equation;
import com.yehor.helper.services.EquationService;

@Controller
@RequestMapping("/")
public class EquationController {

    @Autowired
    EquationService equationService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/saveEquation")
    public String showEqForm() {
        return "save-eq-form";
    }

    @PostMapping("/saveEquation")
    public String saveData(@RequestParam("val") String val, RedirectAttributes redirectAttributes) {
        // Process the form data
        boolean isSuccessful = equationService.addEquation(val);
        if (!isSuccessful) {
            return "redirect:/error";
        }

        // Redirect to the primary view of /save
        // redirectAttributes.addFlashAttribute("message", "Data saved successfully!");
        return "redirect:/";
    }

    @GetMapping("/processEquation")
    public String showProcessEqForm() {
        return "process-eq-form";
    }

    @PostMapping("/processEquation")
    public String processEquation(@RequestParam("val") String val, @RequestParam("root") Double root,
            RedirectAttributes redirectAttributes) {
        // Process the form data
        boolean isSuccessful = equationService.processEquation(val, root);
        if (!isSuccessful) {
            return "redirect:/error";
        }

        // Redirect to the primary view of /save
        // redirectAttributes.addFlashAttribute("message", "Data saved successfully!");
        return "redirect:/";
    }

    @GetMapping("/findEquationsByRoots")
    public String showFindEqByRootsForm() {
        return "find-eq-by-roots-form";
    }

    @PostMapping("/findEquationsByRoots")
    public String findEquationsByRoots(@RequestParam("roots") String roots,
            RedirectAttributes redirectAttributes) {
        List<Equation> equations = equationService.findEquationsByAnyRootFromArray(roots.split("\\s+"));

        redirectAttributes.addFlashAttribute("equations", equations);

        return "redirect:/equations";
    }

    @GetMapping("/findEquationsWithSingleRoot")
    public String showFindEqWithSingleRootForm() {
        return "find-eq-with-single-root-form";
    }

    @PostMapping("/findEquationsWithSingleRoot")
    public String findEquationsWithSingleRoot(RedirectAttributes redirectAttributes) {
        List<Equation> equations = equationService.findEquationsWithSingleRoot();

        redirectAttributes.addFlashAttribute("equations", equations);

        return "redirect:/equations";
    }

    @GetMapping("/equations")
    public String showEquations(Model model) {
        // Retrieve redirected attributes, if any
        List<Equation> equations = (List<Equation>) model.getAttribute("equations");

        // If not redirected, get equations normally
        // if (equations == null) {
        // equations = equationService.getAllEquations();
        // }

        model.addAttribute("equations", equations);

        return "equations"; // Return the name of the Thymeleaf template to render equations
    }

    // @PostMapping("/saveEquation")
    // public ModelAndView saveData(@ModelAttribute String val) {
    // ModelAndView mav = new ModelAndView();
    // mav.setViewName("save-eq-form");
    // mav.addObject("val", val);
    // return mav;
    // }
}
