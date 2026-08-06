package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.models.NivelEntity;
import br.com.ayo_quest.ayo_quest.repository.NivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/niveis")
@RequiredArgsConstructor
public class NivelController {


    private final NivelRepository nivelRepository;


    @GetMapping("/listar")
    public ResponseEntity<List<NivelEntity>> listar(){

        return ResponseEntity.ok(
                nivelRepository.findByStatusTrueOrderByOrdemAsc()
        );

    }

}
