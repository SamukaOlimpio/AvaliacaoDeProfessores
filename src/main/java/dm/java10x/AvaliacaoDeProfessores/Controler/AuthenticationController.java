package dm.java10x.AvaliacaoDeProfessores.Controler;

import dm.java10x.AvaliacaoDeProfessores.dto.*;
import dm.java10x.AvaliacaoDeProfessores.infra.security.TokenService;
import dm.java10x.AvaliacaoDeProfessores.model.*;
import dm.java10x.AvaliacaoDeProfessores.repository.TurmaRepository;
import dm.java10x.AvaliacaoDeProfessores.service.AlunoService;
import dm.java10x.AvaliacaoDeProfessores.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private TokenService tokenService;

    // ============ LOGINS ============


    @PostMapping("/login/aluno")
    public ResponseEntity loginAluno(@RequestBody AuthenticationDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            if (!(userDetails instanceof AlunoModel)) {
                return ResponseEntity.badRequest().body("Usuário não é um aluno");
            }

            String token = tokenService.generateToken(userDetails);
            AlunoModel aluno = (AlunoModel) userDetails;

            return ResponseEntity.ok(new LoginResponseAlunoDTO(
                    token, "aluno", aluno.getNome(), aluno.getEmail(), aluno.getTurma().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }

    @PostMapping("/login/professor")
    public ResponseEntity loginProfessor(@RequestBody AuthenticationDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            if (!(userDetails instanceof ProfessorModel)) {
                return ResponseEntity.badRequest().body("Usuário não é um professor");
            }

            String token = tokenService.generateToken(userDetails);
            ProfessorModel professor = (ProfessorModel) userDetails;

            return ResponseEntity.ok(new LoginResponseProfessorDTO(
                    token, "professor", professor.getNome(), professor.getEmail(),
                    professor.getMateria().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }


    @PostMapping("/register/aluno")
    public ResponseEntity registerAluno(@RequestBody RegisterDTO data){
        try {
            // Verifica se email já existe como aluno
            if(alunoService.findByEmail(data.email()) != null) {
                return ResponseEntity.badRequest().body("Email já cadastrado como aluno");
            }

            // Verifica se email já existe como professor (opcional - para evitar conflito)
            if(professorService.findByEmail(data.email()) != null) {
                return ResponseEntity.badRequest().body("Email já cadastrado como professor. Use outro email.");
            }

            if(! data.email().contains("@")){
                return ResponseEntity.badRequest().build();
            }

            String senhaCripto = new BCryptPasswordEncoder().encode(data.senha());
            AlunoModel aluno = new AlunoModel(data.nome(), data.turma(), senhaCripto, data.email());
            alunoService.create(aluno);

            return ResponseEntity.status(201).body("Aluno registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar aluno: " + e.getMessage());
        }
    }

    @PostMapping("/register/professor")
    public ResponseEntity registerProfessor(@RequestBody RegisterProfessorDTO data){
        try {
            if(professorService.findByEmail(data.email()) != null) {
                return ResponseEntity.badRequest().body("Email já cadastrado como professor");
            }

            if(alunoService.findByEmail(data.email()) != null) {
                return ResponseEntity.badRequest().body("Email já cadastrado como aluno. Use outro email.");
            }

            if (! data.email().contains("@")){
                return ResponseEntity.badRequest().body("Email invalido");
            }

            String senhaCripto = new BCryptPasswordEncoder().encode(data.senha());
            ProfessorModel professor = new ProfessorModel(
                    data.nome(),
                    data.materia(),
                    senhaCripto,
                    data.email());
            professorService.create(professor, data.turmas(), data.file());
            ProfessorModel professorSalvo = professorService.findProfessorModelByEmail(data.email());
            return ResponseEntity.status(201).body("Professor registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar professor: " + e.getMessage());
        }
    }
}