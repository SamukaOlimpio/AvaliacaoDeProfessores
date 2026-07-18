package dm.java10x.AvaliacaoDeProfessores.enumeradores;

public enum Materia {

    LINGUAGENS("linguagens"),
    CIENCIASNATURAIS("cienciasnaturais"),
    CIENCIASHUMANAS("cienciashumanas"),
    MATEMATICA("matematica"),
    BASETECNICA("basetecnica");

    private final String materia;

    Materia (String materia){
        this.materia = materia;
    }
}
