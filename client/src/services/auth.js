/**
 * auth.js — Autenticação do portal CEBE
 * Depende de api.js (deve ser carregado antes deste arquivo).
 */

/**
 * Realiza o login.
 * O back-end espera: { email, senha }
 * Salva o token retornado no localStorage.
 */
async function login(email, senha) {
    const dados = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, senha })
    });
    localStorage.setItem('cebe_token', dados.token);
    return dados;
}

/**
 * Cadastra um novo aluno.
 * O back-end espera: { nome, telefone, cpf, email, senha }
 * A senha será criptografada pelo servidor antes de salvar.
 */
async function cadastrar(nome, telefone, cpf, email, senha) {
    return await apiFetch('/alunos', {
        method: 'POST',
        body: JSON.stringify({ nome, telefone, cpf, email, senha })
    });
}

/**
 * Faz logout limpando o token e redirecionando para o login.
 */
function logout() {
    localStorage.removeItem('cebe_token');
    window.location.href = 'login.html';
}