package dm.java10x.AvaliacaoDeProfessores.service;

import dm.java10x.AvaliacaoDeProfessores.repository.AlunoRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = alunoRepository.findByEmail(username);
        if (user != null) {
            return user;
        }

        user = professorRepository.findByEmail(username);
        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}