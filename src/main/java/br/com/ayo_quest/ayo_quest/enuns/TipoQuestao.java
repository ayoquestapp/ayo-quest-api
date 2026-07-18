package br.com.ayo_quest.ayo_quest.enuns;

public enum TipoQuestao {

    MULTIPLA_ESCOLHA("Apenas uma alternativa correta"),
    CAIXAS_SELECAO("Múltiplas alternativas corretas"),
    VERDADEIRO_FALSO("Verdadeiro ou Falso"),
    QUESTAO_ABERTA("Questão Aberta");

    private final String descricao;

    TipoQuestao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}