package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.DadosProfileDTO;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.service.ProfileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileService service;



    @GetMapping("/tutors")
    public ResponseEntity<List<ProfileEntity>> getTutors() {

        return ResponseEntity.ok(
                service.getTutors()
        );

    }



    @GetMapping("/me")
    public ResponseEntity<DadosProfileDTO> getMyProfile(
            Authentication authentication
    ) {


        return ResponseEntity.ok(
                service.getDados(authentication)
        );

    }



    @GetMapping("/dados-profile")
    public ResponseEntity<DadosProfileDTO> getDadosProfile(
            Authentication authentication
    ) {


        return ResponseEntity.ok(
                service.getDados(authentication)
        );

    }



    @PutMapping("/dados-profile/alterar")
    public ResponseEntity<DadosProfileDTO> updateDadosProfile(
            Authentication authentication,
            @RequestBody DadosProfileDTO dto
    ) {


        return ResponseEntity.ok(
                service.alterarDados(
                        authentication,
                        dto
                )
        );

    }

}