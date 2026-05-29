package dm.java10x.AvaliacaoDeProfessores.dto;

import dm.java10x.AvaliacaoDeProfessores.model.TurmaModel;

import java.util.List;

public record LoginResponseProfessorDTO(String token, String tipo, String nome, String email, String materia) {
}
