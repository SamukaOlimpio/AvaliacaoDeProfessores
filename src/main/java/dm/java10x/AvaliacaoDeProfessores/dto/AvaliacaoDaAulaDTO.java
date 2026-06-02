package dm.java10x.AvaliacaoDeProfessores.dto;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Adjetivo;

public record AvaliacaoDaAulaDTO(Long id_aluno, Long id_professor, Adjetivo  adjetivo, Integer nota) {
}
