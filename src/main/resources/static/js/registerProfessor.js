document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const username = document.querySelector('input[type="text"]');
    const email = document.querySelector('input[type="email"]');
    const password = document.querySelector('input[type="password"]');
    const turmas = document.querySelectorAll('input[type = "checkbox"]');
    const button = document.querySelector('button');

    
    async function registrar(dados) {
         console.log('Enviando dados...');
        try{
            const response = await fetch(`http://localhost:8082/auth/register/professor`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });
            if (response.ok) {
                console.log('Registro com sucesso')
                window.location.href = '../templates/loginProfessor.html';
            }
        }catch(error){
            console.error("Erro na hora de enviar:",error)
        }
    }

    button.addEventListener('click', function () {
        event.preventDefault();
        
        const userNameValid = checkInputUsername();
        const emailValid = checkInputEmail();
        const senhaValid = checkInputPassword();
        const valoresSelecionados = getSelectedValues(turmas);
        
        if (userNameValid && emailValid && senhaValid) {
            const RegisterData = {
                nome: username.value,
                email: email.value,
                senha: password.value,
                materia: "BASETECNICA",
                turmas: valoresSelecionados
            }
    
            registrar(RegisterData);
            
        }

    })

})

function getSelectedValues(turmas) {
    const selected = [];
    turmas.forEach(checkbox => {
        if (checkbox.checked) {
            selected.push(checkbox.value);
        }
    });
    return selected;
}


function checkInputUsername() {
    const usernameValue = username.value;

    if (usernameValue === "") {
        errorInput(username, "Preencha este campo!");
        return false;
    } else {
        const formItem = username.parentElement;
        formItem.className = "input-box"
        return true;
    }

}

function checkInputEmail() {
    const emailValue = email.value;

    if (emailValue === "") {
        errorInput(email, "Preencha este campo!");
        return false;
    } else {
        const formItem = email.parentElement;
        formItem.className = "input-box"
        return true;
    }
}

function checkInputPassword() {
    const passwordValue = password.value;

    if (passwordValue === "") {
        errorInput(password, "Preencha este campo!");
        return false;
    } else if (passwordValue.length < 8) {
        errorInput(password, "No mínimo 8 caracteres!");
        return false;
    } else {
        const formItem = password.parentElement;
        formItem.className = "input-box"
        return true;
    }
}

function checkInputSubject() {
    const subjectValue = subject.value;

    if (subjectValue === "") {
        errorInput(subject, "Preencha este campo!")
    } else {
        const formItem = subject.parentElement;
        formItem.className = "input-box"
    }
}



function errorInput(input, message) {
    const formItem = input.parentElement;
    const textMessage = formItem.querySelector("a");

    textMessage.innerText = message;

    formItem.className = "input-box-error";
}