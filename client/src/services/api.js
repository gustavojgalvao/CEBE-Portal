// Mudar de acordo com o IP do host
const API_URL = "http://localhost:8080";

async function apiFetch(rota, opcoes = {}) {
    const token = localStorage.getItem('cebe_token');
    const resposta = await fetch(API_URL + rota, {
        ...opcoes,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': 'Bearer ' + token } : {}),
            ...opcoes.headers
        }
    });

    // Token expirado ou sem permissão: redireciona para login
    if (resposta.status === 401 || resposta.status === 403) {
        localStorage.removeItem('cebe_token');
        if (!window.location.pathname.includes('login.html')) {
            window.location.href = 'login.html';
        }
        throw { status: resposta.status, message: 'Sessão expirada. Faça login novamente.' };
    }

    if (!resposta.ok) {
        const erro = await resposta.json().catch(() => ({}));
        console.error('Erro da API:', erro);
        throw erro;
    }

    const texto = await resposta.text();
    return texto ? JSON.parse(texto) : null;
}
