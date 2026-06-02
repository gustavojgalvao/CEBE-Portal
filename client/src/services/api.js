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

    if (!resposta.ok) {
        const erro = await resposta.json().catch(() => ({}));
        console.error('Erro da API:', erro);
        throw erro;
    }

    const texto = await resposta.text();
    return texto ? JSON.parse(texto) : null;
}

