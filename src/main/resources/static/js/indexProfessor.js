document.addEventListener('DOMContentLoaded', function() {
    // Função para ler um cookie específico
    function getCookie(nome) {
        const cookies = document.cookie.split('; ');
        for (let cookie of cookies) {
            const [key, value] = cookie.split('=');
            if (key === nome) {
                return decodeURIComponent(value);
            }
        }
        return null;
    }
    
    // Função para apagar todos os cookies
    function deleteAllCookies() {
        const cookies = document.cookie.split('; ');
        
        for (let cookie of cookies) {
            const [nome] = cookie.split('=');
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=${window.location.hostname};`;
        }
        
        const cookiesToDelete = ['auth_token', 'user_name', 'user_email', 'user_turma', 'professor_email'];
        cookiesToDelete.forEach(nome => {
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
        });
        
        console.log('Todos os cookies foram apagados');
    }
    
    // Função para buscar informações do professor (com token)
    async function buscarInfoProfessor(email, token) {
        try {
            const response = await fetch(`http://localhost:8082/professor/buscar/${email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            
            if (response.status === 401 || response.status === 403) {
                console.log('Token inválido ou expirado');
                logout();
                return null;
            }
            
            if (!response.ok) {
                throw new Error(`Erro HTTP: ${response.status}`);
            }
            
            const professor = await response.json();
            return professor;
        } catch (error) {
            console.error('Erro ao buscar professor:', error);
            return null;
        }
    }

    async function buscarModaDeAdjetivo(id, token) {
        try{
            const response = await fetch(`http://localhost:8082/professor/adjetivo/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 401 || response.status === 403) {
                console.log('Token inválido ou expirado');
                logout();
                return null;
            }

            if (!response.ok) {
                throw new Error(`Erro HTTP: ${response.status}`);
            }

            const adjetivo = await response.json();
            return adjetivo;
        } catch (error) {
            console.error('Erro ao buscar adjetivo:', error);
            return null;
        }
    }
    
    // Função para buscar média do professor (com token)
    async function buscarMediaProfessor(id, token) {
        try {
            const response = await fetch(`http://localhost:8082/professor/media/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            
            if (response.status === 401 || response.status === 403) {
                console.log('Token inválido ou expirado');
                logout();
                return null;
            }
            
            if (!response.ok) {
                throw new Error(`Erro HTTP: ${response.status}`);
            }
            
            const media = await response.json();
            return media;
        } catch (error) {
            console.error('Erro ao buscar média:', error);
            return null;
        }
    }
    
    // Função para verificar token no backend (opcional)
    async function verificarToken(token) {
        try {
            const response = await fetch('http://localhost:8082/auth/verificar', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            
            return response.ok;
        } catch (error) {
            console.error('Erro ao verificar token:', error);
            return false;
        }
    }
    
    // Função para exibir os dados na tela
    function exibirDadosProfessor(professor, media, adjetivo) {
        // Exibe o nome do professor
        const nomeElement = document.querySelector('#nomeProfessor');
        if (nomeElement && professor.nome) {
            nomeElement.textContent = professor.nome;
        }
        
     // Exibe o que precisa melhorar
        const melhorarElement = document.querySelector('.melhorar');
        if (melhorarElement && adjetivo !== null) {
            melhorarElement.textContent = adjetivo;
        }
        
        // Exibe a nota total
        const notaElement = document.querySelector('.nota');
        if (notaElement && media !== null) {
            notaElement.textContent = `${media}/10`;
        } else if (notaElement) {
            notaElement.textContent = 'Aguardando avaliações';
        }
    }
    
    // Função principal
    async function carregarDadosProfessor() {
        // Pega o token e email dos cookies
        const token = getCookie('auth_token');
        let professorEmail = getCookie('professor_email') || getCookie('user_email');
        
        if (!token) {
            console.log('Token não encontrado');
            window.location.href = 'loginProfessor.html';
            return;
        }
        
        if (!professorEmail) {
            console.log('Email do professor não encontrado nos cookies');
            window.location.href = 'loginAluno.html';
            return;
        }
        
        console.log('Buscando dados para o professor:', professorEmail);
        
        // Buscar informações do professor
        const professor = await buscarInfoProfessor(professorEmail, token);
        
        if (!professor) {
            console.log('Professor não encontrado ou token inválido');
            return;
        }
        
        console.log('Professor encontrado:', professor);
        
        // Buscar média do professor
        const media = await buscarMediaProfessor(professor.id, token);
        console.log('Média do professor:', media);

        // Buscar adjetivo do professor
        const adjetivo = await buscarModaDeAdjetivo(professor.id, token);
        console.log('Adjetivo do professor:', adjetivo);
        
        // Exibir dados na tela
        exibirDadosProfessor(professor, media, adjetivo);
    }
    
    // Função para fazer logout
    function logout() {
        console.log('Realizando logout...');
        deleteAllCookies();
        window.location.href = 'loginProfessor.html';
    }
    
    // Verifica autenticação e carrega dados
    const token = getCookie('auth_token');
    if (!token) {
        console.log('Token não encontrado. Redirecionando para login...');
        window.location.href = 'loginProfessor.html';
    } else {
        console.log('Usuário autenticado com sucesso!');
        carregarDadosProfessor();
    }
    
    // Adiciona evento ao botão de deslogar
    const logoutButton = document.querySelector('.btn-logout');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault();
            logout();
        });
    } else {
        console.warn('Botão de logout não encontrado');
    }
});