package org.my_tma_test_app.tmaz.controller;

import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.service.TelegramService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final TelegramService telegramService;

    @GetMapping("/main")
    public String mainPage(Model model) {

        //System.out.println("DEBUG HomePageController");


        TelegramUser user = (TelegramUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user = telegramService.save(user);

        //System.out.println("DEBUG telegram user from SecurityContextHolder: " + user);

        model.addAttribute("telegramUser", user);
        return "main";
    }
}
