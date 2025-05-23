package org.my_tma_test_app.tmaz.controller;

import jakarta.servlet.http.HttpSession;
import org.my_tma_test_app.tmaz.security.model.TelegramUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/main")
    public String mainPage(HttpSession httpSession, Model model) {
        TelegramUser user = (TelegramUser) httpSession.getAttribute("telegramUser");
        model.addAttribute("telegramUser", user);
        return "main";
    }
}
