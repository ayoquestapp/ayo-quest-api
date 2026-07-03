package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private final ProfileService service;

    @GetMapping("/tutors")
    public ResponseEntity<List<ProfileEntity>> getTutors() {
        return ResponseEntity.ok(service.getTutors());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {

        System.out.println("=== AUTH DEBUG ===");
        System.out.println("AUTH CLASS: " + authentication.getClass());
        System.out.println("AUTH NAME: " + authentication.getName());
        System.out.println("PRINCIPAL: " + authentication.getPrincipal());
        System.out.println("==================");

        return ResponseEntity.ok(Map.of(
                "authName", authentication.getName(),
                "principalClass", authentication.getPrincipal().getClass().getName()
        ));
    }
}
