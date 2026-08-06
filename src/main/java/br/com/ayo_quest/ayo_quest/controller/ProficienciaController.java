package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.proficiencia.CadastrarProficienciaDTO;
import br.com.ayo_quest.ayo_quest.dto.proficiencia.ProficienciaDTO;
import br.com.ayo_quest.ayo_quest.service.ProficienciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proficiencias")
@RequiredArgsConstructor
public class ProficienciaController {

    private final ProficienciaService proficienciaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ProficienciaDTO> cadastrar(@RequestBody CadastrarProficienciaDTO dto) {
        return ResponseEntity.ok(proficienciaService.cadastrar(dto));
    }

    @GetMapping("listar")
    public ResponseEntity<List<ProficienciaDTO>> listarTodas() {
        return ResponseEntity.ok(proficienciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProficienciaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proficienciaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProficienciaDTO> atualizar(@PathVariable Long id, @RequestBody CadastrarProficienciaDTO dto) {
        return ResponseEntity.ok(proficienciaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        proficienciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}