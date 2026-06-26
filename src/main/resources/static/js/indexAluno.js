// arquivo: indexAluno.js

document.addEventListener('DOMContentLoaded', function() {

    async function pegarProfessores(email, token) {
        try {
            const response = await fetch(`http://localhost:8082/aluno/aulas/${email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 401 || response.status === 403) {
                console.log('Token inválido ou expirado');
                console.log(token);
                logout();
                return null;
            }

            if (!response.ok) {
                throw new Error(`Erro HTTP: ${response.status}`);
            }

            const professoresASeremAvaliados = await response.json();
            return professoresASeremAvaliados;
        } catch (error) {
            console.error('Dados não carregados', error);
            return null;
        }
    }

    async function enviarAvaliacao(dados, token){
        try{
            const response = await fetch(`http://localhost:8082/aluno/aula`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(dados)
            });

            if(response.ok){
            location.reload();
        }
        } catch (error){
            console.log(error);
        }
    }
    function getCookie(nome) {
        const cookies = document.cookie.split('; ');
        for (let cookie of cookies) {
            const [key, value] = cookie.split('=');
            if (key === nome) {
                return value;
            }
        }
        return null;
    }

    function deleteAllCookies() {
        const cookies = document.cookie.split('; ');
        for (let cookie of cookies) {
            const [nome] = cookie.split('=');
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=${window.location.hostname};`;
        }

        const cookiesToDelete = ['auth_token', 'user_name', 'user_email', 'user_turma'];
        cookiesToDelete.forEach(nome => {
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
            document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=${window.location.hostname};`;
        });

        console.log('Todos os cookies foram apagados');
    }

    function logout() {
        console.log('Realizando logout...');
        deleteAllCookies();
        window.location.href = 'loginAluno.html';
    }

    async function receberProfessores(token) {
        const email = getCookie('user_email');
        const professoresParaAvaliar = await pegarProfessores(email, token);
        
        if (!professoresParaAvaliar) {
            return [];
        }
        
        // Transforma para apenas nome e id
        const resultado = professoresParaAvaliar.map(item => ({
            nome: item.nome,
            id: item.id
        }));
        
        return resultado;
    }

    // Função para armazenar os professores em uma variável global
    let listaProfessores = [];

    async function carregarProfessores(token) {
        try {
            const dadosDeProfessores = await receberProfessores(token);
            listaProfessores = dadosDeProfessores; // Armazena globalmente
            
            const divProfessores = document.getElementById('professores');
            
            if (!dadosDeProfessores || dadosDeProfessores.length === 0) {
                divProfessores.innerHTML = `
                    <div class="professor">
                        <p>Nenhum professor encontrado</p>
                    </div>
                `;
                return;
            }
            
            divProfessores.innerHTML = '';
            
            // CORREÇÃO: Adiciona o ID como atributo data-id
            dadosDeProfessores.forEach(({ nome, id }) => {
                divProfessores.innerHTML += `
                    <div class="professor" data-id="${id}">
                        <p>${nome}</p>
                    </div>
                `;
            });
            
            // Adiciona eventos após criar os elementos
            adicionarEventosAosProfessores();
            
        } catch (erro) {
            console.error('Erro ao carregar professores:', erro);
            const divProfessores = document.getElementById('professores');
            divProfessores.innerHTML = `
                <div class="professor">
                    <p style="color: red;">Erro ao carregar professores</p>
                </div>
            `;
        }
    }

    // Função para adicionar eventos aos professores
    function adicionarEventosAosProfessores() {
        const professores = document.querySelectorAll('.professor');
        console.log('Professores encontrados:', professores.length);

        professores.forEach((professor) => {
            professor.addEventListener('click', function() {
                const temProfessorSelecionado = Array.from(professores).some(p => p.classList.contains('selecionadoProfessor'));
                if (temProfessorSelecionado) {
                    const professorSelecionado = document.querySelector('.selecionadoProfessor');
                    professorSelecionado.style.color = "black";
                    professorSelecionado.style.backgroundColor = "white";
                    professorSelecionado.classList.remove('selecionadoProfessor');
                }

                professor.classList.add('selecionadoProfessor');
                professor.style.color = "white";
                professor.style.backgroundColor = "black";
            });
        });
    }

    // Verifica se o token existe
    const token = getCookie('auth_token');
    
    if (!token) {
        console.log('Token não encontrado. Redirecionando para login...');
        window.location.href = 'loginAluno.html';
    } else {
        console.log('Usuário autenticado com sucesso!');
        carregarProfessores(token);
    }

    // Adiciona evento ao botão de deslogar
    const logoutButton = document.querySelector('button[id="logout"]');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault();
            logout();
        });
    } else {
        console.warn('Botão de logout não encontrado');
    }

    // Adicionar evento de clique para cada nota
    const notas = document.querySelectorAll('.nota');
    const listaDeCoresDasNotas = ['red', 'green', 'blue', 'yellow', 'purple', 'orange', 'pink', 'brown', 'gray', 'burlywood'];

    notas.forEach((nota, index) => {
        nota.addEventListener('click', function() {
            const temNotaSelecionada = Array.from(notas).some(n => n.classList.contains('selecionadaNota'));
            if (temNotaSelecionada) {
                const indexNotaSelecionada = Array.from(notas).findIndex(n => n.classList.contains('selecionadaNota'));
                const notaSelecionada = document.querySelector('.selecionadaNota');
                notaSelecionada.style.color = "black";
                notaSelecionada.style.backgroundColor = listaDeCoresDasNotas[indexNotaSelecionada];
                notaSelecionada.classList.remove('selecionadaNota');
            }
            
            nota.classList.add('selecionadaNota');
            nota.style.color = "white";
            nota.style.backgroundColor = "black";
        });
    });

    const comentarios = document.querySelectorAll('.comentario');
    const listaDeCoresDosComentarios = ['red', 'green', 'blue', 'yellow', 'purple'];
    const opcoes = ['OTIMO', 'BOM', 'MEDIO', 'RUIM', 'PESSIMO'];

    comentarios.forEach((comentario, index) => {
        comentario.addEventListener('click', function() {
            const temComentarioSelecionado = Array.from(comentarios).some(c => c.classList.contains('selecionadoComentario'));
            if (temComentarioSelecionado) {
                const indexComentarioSelecionado = Array.from(comentarios).findIndex(c => c.classList.contains('selecionadoComentario'));
                const comentarioSelecionado = document.querySelector('.selecionadoComentario');
                comentarioSelecionado.style.color = "black";
                comentarioSelecionado.style.backgroundColor = listaDeCoresDosComentarios[indexComentarioSelecionado];
                comentarioSelecionado.classList.remove('selecionadoComentario');
            }

            comentario.classList.add('selecionadoComentario');
            comentario.style.color = "white";
            comentario.style.backgroundColor = "black";
        });
    });

    // BOTÃO AVALIAR - CORRIGIDO PARA PEGAR O ID
    const botao = document.querySelector('#btn-avaliar');

    botao.addEventListener('click', function() {
        const professores = document.querySelectorAll('.professor');
        let temNotaSelecionada = Array.from(notas).some(n => n.classList.contains('selecionadaNota'));
        let temComentarioSelecionado = Array.from(comentarios).some(c => c.classList.contains('selecionadoComentario'));
        let temProfessorSelecionado = Array.from(professores).some(p => p.classList.contains('selecionadoProfessor'));
        
        if (!temNotaSelecionada) {
            window.alert("Nota não foi selecionada");
        } else if (!temComentarioSelecionado) {
            window.alert("Comentário não foi selecionado");
        } else if (!temProfessorSelecionado) {
            window.alert("Professor não foi selecionado");
        } else {
            const notaSelecionada = Array.from(notas).findIndex(n => n.classList.contains('selecionadaNota'));
            const comentarioSelecionado = Array.from(comentarios).findIndex(c => c.classList.contains('selecionadoComentario'));
            
            // CORREÇÃO: Pega o ID do professor selecionado
            const professorSelecionado = document.querySelector('.selecionadoProfessor');
            const professorId = professorSelecionado ? professorSelecionado.dataset.id : null;
            
            // Pega o nome do professor (opcional)
            const professorNome = professorSelecionado ? professorSelecionado.querySelector('p').textContent : null;
            
            console.log("Tudo concluído!");
            console.log(`Professor ID: ${professorId}`);
            console.log(`Professor Nome: ${professorNome}`);
            console.log(`Nota: ${notaSelecionada + 1}`);
            console.log(`Comentário: ${opcoes[comentarioSelecionado]}`);
            
            // Aqui você pode enviar os dados para o servidor
            const dadosAvaliacao = {
                id_professor: professorId,
                email: getCookie('user_email'),
                nota: notaSelecionada + 1,
                adjetivo: opcoes[comentarioSelecionado]
            };
            enviarAvaliacao(dadosAvaliacao, token);
            console.log('Dados da avaliação:', dadosAvaliacao);
            
           
        }
    });
});