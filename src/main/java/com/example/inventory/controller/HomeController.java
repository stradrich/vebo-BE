package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {


   // LANDING PAGE
    // @GetMapping("/home")
    // public String eample(Model model) {
    //     model.addAttribute("name", "Inventory App");
    //     return "home"; 
    // }

    @GetMapping("/home-page")
    public String homePage(Model model) {
        System.out.println("Home page requested");
          // Add a name attribute to the model
          // Initial load with a default name
          model.addAttribute("name", "Guest");
        return "home-page";  // This should resolve to src/main/resources/templates/home-page.html
    }

     @PostMapping("/home-page")
    public String updateName(@RequestParam("name") String name, Model model) {
        // Pass the entered name back to the model
        model.addAttribute("name", name);
        return "home-page"; // Reloads the same page with the updated name
    }
}