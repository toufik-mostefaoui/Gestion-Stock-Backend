package com.example.gestionstock.controller;

import com.example.gestionstock.dto.loginDTO;
import com.example.gestionstock.dto.singupDTO;
import com.example.gestionstock.entity.User;
import com.example.gestionstock.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody singupDTO signupDTO){
        User user = this.authService.signup(signupDTO.getUsername(), signupDTO.getEmail(), signupDTO.getPassword());

        return "User signed up successfully ";
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody loginDTO loginDTO){

        return this.authService.login(loginDTO.getEmail(), loginDTO.getPassword());

    };

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> req) {
        return authService.refresh(req.get("refreshToken"));
    }

    @GetMapping("/admin/test")
    public String test(){
        return "this is test";
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestBody Map<String, String> request) {
        authService.logout(request.get("refreshToken"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");

        return response;
    }
}
