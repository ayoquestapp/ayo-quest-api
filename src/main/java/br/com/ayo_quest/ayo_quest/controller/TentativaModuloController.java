package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.IniciarTentativaDTO;
import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.TentativaDTO;
import br.com.ayo_quest.ayo_quest.service.TentativaModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/tentativas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TentativaModuloController {


    private final TentativaModuloService service;


    @PostMapping("/iniciar")
    public ResponseEntity<TentativaDTO> iniciar(
            @RequestBody IniciarTentativaDTO dto
    ) {


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        UUID profileId =
                UUID.fromString(authentication.getName());


        return ResponseEntity.ok(
                service.iniciar(dto, profileId)
        );

    }

}
