package dm.java10x.AvaliacaoDeProfessores.service;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Adjetivo;
import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;
import dm.java10x.AvaliacaoDeProfessores.model.*;
import dm.java10x.AvaliacaoDeProfessores.repository.AvaliacaoRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.ImageRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.ProfessorRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.TurmaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProfessorService {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private ImageRepository image;

    public List<ProfessorModel> findAll(){
        return professorRepository.findAll();
    }

    public ProfessorModel findById(long id){
        Optional<ProfessorModel> professorModel = this.professorRepository.findById(id);
        return professorModel.orElseThrow(() -> new RuntimeException(
                "Professor não encontrado"
        ));
    }

    public UserDetails findByEmail(String email){
        return professorRepository.findByEmail(email);
    }

    public ProfessorModel findProfessorModelByEmail(String email){
        return professorRepository.findProfessorModelByEmail(email);
    }

    @Transactional
    public ProfessorModel create(ProfessorModel obj, List<Turma> turmas, MultipartFile file){
        obj = this.professorRepository.save(obj);
        for (Turma t: turmas){
            TurmaModel novaTurma = new TurmaModel(t, obj);
            turmaRepository.save(novaTurma);
        }
        try {
            uploadImage(file, obj);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel salvar  imagem", e);
        }
        return obj;
    }

    @Transactional
    public ProfessorModel update(ProfessorModel obj){
        ProfessorModel newProfessor = findById(obj.getId());
        if(Objects.nonNull(obj.getSenha())){newProfessor.setSenha(obj.getSenha());}
        if (Objects.nonNull(obj.getEmail())){newProfessor.setEmail(obj.getEmail());}
        if (Objects.nonNull(obj.getMateria())){newProfessor.setMateria(obj.getMateria());}
        if (Objects.nonNull(obj.getNome())){newProfessor.setNome(obj.getNome());}
        return this.professorRepository.save(newProfessor);
    }

    @Transactional
    public void delete(long id){
        ProfessorModel professor = findById(id);
        try {
            this.avaliacaoRepository.deleteByProfessorModel(professor);
            this.turmaRepository.deleteByProfessorModel(professor);
            this.professorRepository.deleteById(id);
        } catch (Exception e){
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas");
        }
    }

    public Integer mediaDoProfessorPorId(long id){
        ProfessorModel professor = findById(id);
        int cont = 0;
        int soma = 0;
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByProfessorModel(professor);
        for (AvaliacaoModel avaliacao: avaliacoes){
            soma += avaliacao.getAulaModel().getNota();
            cont ++;
        }
        if (cont > 0){return soma/cont;}
        else{ return 0;}
    }

    public Adjetivo modaDosAdjetivos(long id){
        ProfessorModel professor = findById(id);
        Adjetivo[] adjetivos = {Adjetivo.OTIMO, Adjetivo.BOM, Adjetivo.MEDIO, Adjetivo.RUIM};
        Integer[] quantAdjetivos = {0, 0, 0, 0};
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByProfessorModel(professor);
        for (AvaliacaoModel avaliacao: avaliacoes){
            if (avaliacao.getAulaModel().getAdjetivo().equals(adjetivos[0])){
                quantAdjetivos[0] ++;
            }
            else if (avaliacao.getAulaModel().getAdjetivo().equals(adjetivos[1])){
                quantAdjetivos[1] ++;
            }
            else if (avaliacao.getAulaModel().getAdjetivo().equals(adjetivos[2])){
                quantAdjetivos[2] ++;
            }
            else {quantAdjetivos[3] ++;}
        }
        int max = 0;
        int adj = 0;
        for (int i = 0; i < 4; i++) {
            if (quantAdjetivos[i] > max){
                max = quantAdjetivos[i];
                adj = i;
            }
        }
        return adjetivos[adj];
    }
    public List<ProfessorModel> filtrarPorTurma(Turma turma){
        List<TurmaModel> turmas = turmaRepository.findTurmaModelByTurma(turma);
        List<ProfessorModel> listaDeProfessores = new ArrayList<>();
        for (TurmaModel turminha: turmas){
            if (! listaDeProfessores.contains(turminha.getProfessorModel())){
                listaDeProfessores.add(turminha.getProfessorModel());}
        }
        return listaDeProfessores;
    }

    public List<ProfessorModel> filtrarProfessoresNaoAvaliadosEstaSemana(List<ProfessorModel> professores, AlunoModel aluno){
        List<ProfessorModel> professoresFiltrados = new ArrayList<>();
        for (ProfessorModel professor: professores){
            List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByProfessorModelAndAlunoModel(professor, aluno);
            if (!avaliacaoService.foiAvaliadoNessaSemana(avaliacoes)){
                professoresFiltrados.add(professor);
            }
        }
        return  professoresFiltrados;
    }
    @Transactional
    public Image uploadImage(MultipartFile file, ProfessorModel professor) throws IOException {
        Image imageData = new Image(professor.getNome(), file.getContentType(), file.getBytes(), professor);
        return imageData;
    }

    public Image downloadImage(Long id) {
        Optional<Image> imagem = this.image.findById(id);
        if (imagem.isPresent()) {
            return imagem.get();
        }
        else {
            return imagem.orElseThrow(() -> new RuntimeException("Imagem não encontrada"));
        }
    }
}
