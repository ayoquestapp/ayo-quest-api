package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.EnviarRespostaDTO;
import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.ResultadoRespostaDTO;
import br.com.ayo_quest.ayo_quest.service.RespostaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/respostas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RespostaController {


    private final RespostaService service;


    @PostMapping("/finalizar")
    public ResponseEntity<ResultadoRespostaDTO> finalizar(
            @RequestBody EnviarRespostaDTO dto
    ){

        return ResponseEntity.ok(
                service.finalizar(dto)
        );

    }

}
