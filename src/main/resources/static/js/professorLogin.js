// arquivo: ../static/js/alunologin.js

// Aguarda o DOM carregar completamente
document.addEventListener('DOMContentLoaded', function() {
    // Seleciona o formulário
    const form = document.querySelector('form');
    const emailInput = document.querySelector('input[type="email"]');
    const passwordInput = document.querySelector('input[type="password"]');
    const submitButton = document.querySelector('button[type="submit"]');
    
    // URL do backend
    const API_URL = 'http://localhost:8082/auth/login/professor';
    
    // Adiciona evento de submit ao formulário
    form.addEventListener('submit', async function(event) {
        event.preventDefault(); // Impede o recarregamento da página
        
        // Pega os valores do formulário
        const email = emailInput.value.trim();
        const password = passwordInput.value;
        
        // Desabilita o botão durante o envio
        submitButton.disabled = true;
        submitButton.textContent = 'Enviando...';
        
        try {
            // Prepara os dados para enviar ao backend
            const dadosLogin = {
                login: email,
                senha: password
            };
            
            // Envia a requisição para o backend
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosLogin)
            });
            
            // Verifica se a resposta foi bem sucedida
            if (response.ok) {
                // Tenta fazer o parse da resposta como JSON
                const data = await response.json();
                
                // Verifica se o token existe na resposta
                if (data.token) {
                    // Guarda o token em um cookie (válido por 7 dias)
                    setCookie('auth_token', data.token, 1);
                    
                    // Opcional: guarda também os dados do usuário
                    setCookie('user_name', data.nome, 1);
                    setCookie('user_email', data.email, 1);
                    setCookie('user_turma', data.turma, 1);
                    
                    // Redireciona para index.html
                    window.location.href = '../templates/indexProfessor.html';
                } else {
                    mostrarMensagem('Token não recebido do servidor', 'error');
                }
            } else {
                // Se a resposta não for ok, tenta pegar a mensagem de erro
                let mensagemErro;
                try {
                    const erroTexto = await response.text();
                    mensagemErro = erroTexto;
                } catch (e) {
                    mensagemErro = 'Erro ao fazer login. Verifique suas credenciais.';
                }
                mostrarMensagem(mensagemErro, 'error');
            }
            
        } catch (error) {
            console.error('Erro na requisição:', error);
            mostrarMensagem('Erro ao conectar com o servidor. Verifique se o backend está rodando.', 'error');
        } finally {
            // Reabilita o botão
            submitButton.disabled = false;
            submitButton.textContent = 'Fazer Login';
        }
    });
    
    // Função para guardar cookie
    function setCookie(nome, valor, dias) {
        const data = new Date();
        data.setTime(data.getTime() + (dias * 24 * 60 * 60 * 1000));
        const expires = `expires=${data.toUTCString()}`;
        document.cookie = `${nome}=${valor}; ${expires}; path=/`;
    }
    
    // Função para mostrar mensagem amigável
    function mostrarMensagem(mensagem, tipo) {
        // Remove qualquer mensagem existente
        const mensagemExistente = document.querySelector('.mensagem-flutuante');
        if (mensagemExistente) {
            mensagemExistente.remove();
        }
        
        // Cria o elemento da mensagem
        const mensagemDiv = document.createElement('div');
        mensagemDiv.className = `mensagem-flutuante mensagem-${tipo}`;
        
        // Estiliza a mensagem
        mensagemDiv.style.position = 'fixed';
        mensagemDiv.style.top = '20px';
        mensagemDiv.style.left = '50%';
        mensagemDiv.style.transform = 'translateX(-50%)';
        mensagemDiv.style.padding = '12px 20px';
        mensagemDiv.style.borderRadius = '5px';
        mensagemDiv.style.fontSize = '14px';
        mensagemDiv.style.fontWeight = 'bold';
        mensagemDiv.style.zIndex = '1000';
        mensagemDiv.style.textAlign = 'center';
        mensagemDiv.style.minWidth = '300px';
        mensagemDiv.style.maxWidth = '80%';
        mensagemDiv.style.boxShadow = '0 2px 5px rgba(0,0,0,0.2)';
        
        // Define as cores baseado no tipo
        if (tipo === 'error') {
            mensagemDiv.style.backgroundColor = '#ff4444';
            mensagemDiv.style.color = 'white';
        } else {
            mensagemDiv.style.backgroundColor = '#4CAF50';
            mensagemDiv.style.color = 'white';
        }
        
        mensagemDiv.textContent = mensagem;
        
        // Adiciona a mensagem ao body
        document.body.appendChild(mensagemDiv);
        
        // Remove a mensagem após 5 segundos
        setTimeout(() => {
            mensagemDiv.style.opacity = '0';
            mensagemDiv.style.transition = 'opacity 0.5s';
            setTimeout(() => {
                if (mensagemDiv.parentNode) {
                    mensagemDiv.remove();
                }
            }, 500);
        }, 5000);
    }
});