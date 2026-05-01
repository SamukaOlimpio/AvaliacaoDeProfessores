package dm.java10x.AvaliacaoDeProfessores.model;


import dm.java10x.AvaliacaoDeProfessores.enumeradores.Materia;
import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_professor")
public class ProfessorModel implements UserDetails {

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private Materia materia;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(name ="senha" ,nullable = false)
    private String senha;

    @ElementCollection
    @CollectionTable(
            name = "professor_turmas",
            joinColumns = @JoinColumn(name = "professor_id")
    )
    @Column(name = "turma", nullable = false)
    private List<String> turma;

    public ProfessorModel() {
    }

    public ProfessorModel(String nome, Materia materia, String senha, List<String> turma) {
        this.nome = nome;
        this.materia = materia;
        this.senha = senha;
        this.turma = turma;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getTurma() {
        return turma;
    }

    public void setTurma(List<String> turma) {
        this.turma = turma;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("professor"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
