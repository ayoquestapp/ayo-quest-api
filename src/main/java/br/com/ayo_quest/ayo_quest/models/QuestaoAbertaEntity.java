package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.Entity;

@Entity
public class QuestaoAbertaEntity extends QuestaoEntity {

    private String respostaEsperada;
}