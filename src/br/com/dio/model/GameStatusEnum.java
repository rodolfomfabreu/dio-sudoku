package br.com.dio.model;

public enum GameStatusEnum {
    NON_STARTED("Não iniciado"),
    COMPLETED("Concluído"),
    INCOMPLETED("Não concluído");

    private String label;

    GameStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
