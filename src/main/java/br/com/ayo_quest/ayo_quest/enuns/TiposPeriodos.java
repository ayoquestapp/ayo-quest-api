package br.com.ayo_quest.ayo_quest.enuns;

public enum TiposPeriodos {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");


    private final String label;

    TiposPeriodos(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
