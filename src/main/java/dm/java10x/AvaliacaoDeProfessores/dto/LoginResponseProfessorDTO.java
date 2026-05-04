package dm.java10x.AvaliacaoDeProfessores.dto;

import java.util.List;

public record LoginResponseProfessorDTO(String token, String tipo, String nome, String email, String materia, List<String> turmas) {
}
