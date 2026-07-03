package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.ModuloDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.service.ModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modulos")
@CrossOrigin("*")
public class ModuloController {

    @Autowired
    private ModuloService service;

    @PostMapping("/cadastrar")
    public ModuloEntity salvar(@RequestBody ModuloEntity modulo) {
        return service.salvar(modulo);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ModuloDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarModulo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("alterar/{id}")
    public ModuloEntity atualizar(@PathVariable Long id, @RequestBody ModuloEntity modulo) {
        return service.atualizar(id, modulo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloEntity> buscarPorId(@PathVariable Long id) {
        ModuloEntity modulo = service.buscarPorId(id);
        return ResponseEntity.ok(modulo);
    }
}
