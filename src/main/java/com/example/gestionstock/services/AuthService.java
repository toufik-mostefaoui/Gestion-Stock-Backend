package com.example.gestionstock.services;

import com.example.gestionstock.Security.JwtUtil;
import com.example.gestionstock.entity.RefreshToken;
import com.example.gestionstock.entity.Role;
import com.example.gestionstock.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User signup(String username,String email, String password){
        if (userRepository.existsByEmail(email)){
            System.out.println("Email exists");
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.CLIENT);
        return userRepository.save(user);
    };

    public Map<String, String> login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Email or password incorrect")
        );

        if (!passwordEncoder.matches(password, user.getPassword()
        )) {
            throw new RuntimeException("Email or password incorrect");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        RefreshToken refreshToken = new RefreshToken();
        String refreshTokenValue = jwtUtil.generateRefreshToken(user.getEmail());
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setEmail(user.getEmail());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        refreshToken.setRevoked(false);

            refreshTokenRepository.save(refreshToken);

            Map<String , String > res = new HashMap<>();
            res.put("access token", accessToken);
            res.put("refresh token", refreshToken.getToken());
            res.put("role", user.getRole().name());
            return res;

    };


    public RefreshToken createRefreshToken(String email) {

        RefreshToken refreshToken = new RefreshToken();

        String refreshTokenValue =
                jwtUtil.generateRefreshToken(email);

        refreshToken.setToken(refreshTokenValue);
        refreshToken.setEmail(email);

        refreshToken.setExpiryDate(
                Instant.now().plusSeconds(7 * 24 * 60 * 60)
        );

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }


    public Map<String, String> refresh(String refreshToken) {

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() ->
                                new RuntimeException("Refresh token not found")
                        );

        if (storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        String email = jwtUtil.getRefreshEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        Map<String, String> response = new HashMap<>();

        response.put("access token", accessToken);
        response.put("refresh token", refreshToken);

        return response;
    }

    public void logout(String refreshToken) {

        RefreshToken storedrefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found")
                );

        storedrefreshToken.setRevoked(true);

        refreshTokenRepository.save(storedrefreshToken);
    }
}