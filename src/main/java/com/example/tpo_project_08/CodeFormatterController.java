package com.example.tpo_project_08;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class CodeFormatterController {

    private final JavaCodeFormatter formatter;
    private final CodeStorageService storageService;

    public CodeFormatterController(JavaCodeFormatter formatter, CodeStorageService storageService) {
        this.formatter = formatter;
        this.storageService = storageService;
    }

    @GetMapping("/codeFormatter")
    public String showForm(Model model) {
        model.addAttribute("content", "form");  // Set the fragment to load in the layout
        model.addAttribute("inputCode", "");
        return "layout";  // Return the layout template
    }

    @PostMapping("/codeFormatter/format")
    public String formatCode(
            @RequestParam("code") String code,
            @RequestParam("textId") String textId,
            @RequestParam("expirationSeconds") long expirationSeconds,
            Model model
    ) {
        try {
            String formatted = formatter.format(code);

            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationSeconds);
            SavedCode saved = new SavedCode(textId, code, formatted, expiresAt);
            storageService.saveCode(saved);

            model.addAttribute("original", code);
            model.addAttribute("formatted", formatted);
            model.addAttribute("message", "Saved with ID: " + textId);
        } catch (Exception e) {
            model.addAttribute("error", "Formatting failed: " + e.getMessage());
        }
        model.addAttribute("content", "form");  // Set the fragment to load in the layout
        return "layout";  // Return the layout template
    }

    @GetMapping("/codeFormatter/load")
    public String loadCode(@RequestParam("id") String id, Model model) {
        try {
            SavedCode savedCode = storageService.loadCode(id);
            if (savedCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                model.addAttribute("error", "Code with ID " + id + " has expired.");
                model.addAttribute("content", "form");  // Set the fragment to load in the layout
                return "layout";  // Return the layout template
            }

            model.addAttribute("original", savedCode.getOriginalCode());
            model.addAttribute("formatted", savedCode.getFormattedCode());
            model.addAttribute("message", "Code loaded successfully with ID: " + id);
            model.addAttribute("content", "form");  // Set the fragment to load in the layout
        } catch (IOException | ClassNotFoundException e) {
            model.addAttribute("error", "Error loading code: " + e.getMessage());
        }
        return "layout";  // Return the layout template
    }
}

