package ru.otus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookPageController {
    @GetMapping("/")
    public String getBooks(Model model) {
        return "books";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        return "add";
    }

    @GetMapping("/edit")
    public String saveBookForm(@RequestParam("id") String id,  Model model) {
        model.addAttribute("book_id", id);
        return "edit";
    }
}
