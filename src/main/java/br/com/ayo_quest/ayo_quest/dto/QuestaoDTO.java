package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestaoDTO {

    private String tipo;

    private String enunciado;

    private Integer xp;

    private List<AlternativaDTO> alternativas;

}