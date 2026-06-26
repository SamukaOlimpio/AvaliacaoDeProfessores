package dm.java10x.AvaliacaoDeProfessores.service;

import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.repository.AlunoRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.AvaliacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<AlunoModel> findAll() {
        return this.alunoRepository.findAll();
    }


    public AlunoModel findById(Long id){
        Optional<AlunoModel> alunoModel = this.alunoRepository.findById(id);
        return alunoModel.orElseThrow(() -> new RuntimeException(
                "Aluno não encontrado"
        ));
    }

    public UserDetails findByEmail(String email){
        return alunoRepository.findByEmail(email);
    }

    public AlunoModel findAlunoModelByEmail(String email){
        return alunoRepository.findAlunoModelByEmail(email);
    }

    @Transactional
    public AlunoModel create(AlunoModel obj){
        obj = this.alunoRepository.save(obj);
        return obj;
    }

    @Transactional
    public AlunoModel update(AlunoModel obj){
        AlunoModel newObj = findById(obj.getId());
        if(Objects.nonNull(obj.getNome())){newObj.setNome(obj.getNome());}
        if(Objects.nonNull(obj.getEmail())){newObj.setEmail(obj.getEmail());}
        if(Objects.nonNull(obj.getSenha())){newObj.setSenha(obj.getSenha());}
        if(Objects.nonNull(obj.getTurma())){newObj.setTurma(obj.getTurma());}
        return this.alunoRepository.save(newObj);
    }

    @Transactional
    public void delete(long id){
        AlunoModel aluno = findById(id);
        try {
            this.avaliacaoRepository.deleteByAlunoModel(aluno);
            this.alunoRepository.deleteById(id);
        } catch (Exception e){
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas");
        }
    }

}