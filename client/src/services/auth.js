/**
 * auth.js — Autenticação do portal CEBE
 * Depende de api.js (deve ser carregado antes deste arquivo).
 *
 * Login:    CPF + data de nascimento (formato: DDMMYYYY, ex: "01011990")
 * Cadastro: nome, telefone, cpf, email, dataNascimento
 */

/**
 * Realiza o login com CPF e data de nascimento.
 * O back-end espera: { cpf, dataNascimento }
 */
async function login(cpf, dataNascimento) {
    // Remove pontos e traços do CPF antes de enviar (ex: "123.456.789-09" → "12345678909")
    const cpfLimpo = cpf.replace(/\D/g, '');
    const dados = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ cpf: cpfLimpo, dataNascimento })
    });
    localStorage.setItem('cebe_token', dados.token);
    return dados;
}

/**
 * Cadastra um novo aluno.
 * O back-end espera: { nome, telefone, cpf, email, dataNascimento }
 * O aniversário (dataNascimento) vira a senha inicial, criptografada pelo servidor.
 */
async function cadastrar(nome, telefone, cpf, email, dataNascimento) {
    const cpfLimpo = cpf.replace(/\D/g, '');
    return await apiFetch('/alunos', {
        method: 'POST',
        body: JSON.stringify({ nome, telefone, cpf: cpfLimpo, email, dataNascimento })
    });
}

/**
 * Faz logout limpando o token e redirecionando para o login.
 */
function logout() {
    localStorage.removeItem('cebe_token');
    window.location.href = 'login.html';
}