package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.Entity;

@Entity
public class VerdadeiroFalsoEntity extends QuestaoEntity {

    private boolean resposta;
}