package dm.java10x.AvaliacaoDeProfessores.dto;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;

public record RegisterDTO (String nome, Turma turma, String senha, String email){
}
