package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.ResultadoModuloDTO;
import br.com.ayo_quest.ayo_quest.service.CorrigirModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/correcao-modulo")
@RequiredArgsConstructor
public class CorrigirModuloController {


    private final CorrigirModuloService service;


    @PostMapping("/{id}/corrigir")
    public ResponseEntity<ResultadoModuloDTO> corrigir(

            @PathVariable Long id,

            @RequestParam Long tentativaId,

            @RequestBody Map<String,Object> respostas

    ){

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        UUID profileId =
                UUID.fromString(authentication.getName());


        ResultadoModuloDTO resultado =
                service.corrigir(
                        id,
                        tentativaId,
                        profileId,
                        respostas
                );


        return ResponseEntity.ok(resultado);
    }

}
