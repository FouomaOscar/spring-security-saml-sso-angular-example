package com.easyup.samldemo.controllers;

import com.easyup.samldemo.models.AuthDto;
import com.easyup.samldemo.models.UserDto;
import com.easyup.samldemo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin("*")
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth")
    public void authenticate(Model model, @AuthenticationPrincipal Saml2AuthenticatedPrincipal principal,
                      HttpServletResponse response) throws IOException {
        String emailAddress = principal.getFirstAttribute("emailAddress");
        String firstName = principal.getFirstAttribute("firstName");
        model.addAttribute("emailAddress", emailAddress);
        model.addAttribute("userAttributes", principal.getAttributes());
        AuthDto authDto = new AuthDto(emailAddress, firstName);
        String jwt = userService.authenticate(authDto).getAuthToken();

        response.sendRedirect("http://localhost:4200/auth?token=" + jwt);
    }

    @GetMapping("/api/authUser")
    public UserDto getName(@RequestHeader(name="Authorization") String token) {
        return userService.getAuthUser(token);
    }
}
