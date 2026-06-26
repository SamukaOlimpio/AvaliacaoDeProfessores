document.addEventListener('DOMContentLoaded', function(){
    
    const nome = document.querySelector('input[type="text"]');
    
    const email = document.querySelector('input[type="email"]');
    const senha = document.querySelector('input[type="password"]');
    const turma = document.querySelector('select')
    const botao = document.querySelector('button')

    async function registrar(dados) {
        console.log('Enviando dados...');
        try{
            const response = await fetch(`http://localhost:8082/auth/register/aluno`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });
            if (response.ok) {
                console.log('Registro com sucesso')
                window.location.href = '../templates/loginAluno.html';
            }
        }catch(error){
            console.error("Erro na hora de enviar:",error)
        }
    }

    

    botao.addEventListener('click', function(){
        event.preventDefault();
        if (!nome.value || !email.value || !senha.value || !turma.value) {
        alert('Preencha todos os campos!');
        return;
    }

    const dadosDeRegistro = {
        nome:nome.value,
        email:email.value,
        senha:senha.value,
        turma:turma.value
    }

    registrar(dadosDeRegistro);

    })
})