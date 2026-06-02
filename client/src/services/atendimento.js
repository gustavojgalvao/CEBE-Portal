// Lista todos os atendimentos
async function listarAtendimentos() {
    return await apiFetch('/atendimentos');
}

// Busca um atendimento específico pelo seu ID.
async function buscarAtendimentoPorId(id) {
    return await apiFetch(`/atendimentos/${id}`);
}

/*
 Cria um novo chamado de atendimento.
 O back-end espera um objeto Atendimento no body.
*/
async function criarAtendimento(dadosAtendimento) {
    return await apiFetch('/atendimentos', {
        method: 'POST',
        body: JSON.stringify(dadosAtendimento)
    });
}

// Atualiza um atendimento existente.
async function atualizarAtendimento(id, dadosAtendimento) {
    return await apiFetch(`/atendimentos/${id}`, {
        method: 'PUT',
        body: JSON.stringify(dadosAtendimento)
    });
}

// Deleta/exclui um chamado pelo seu ID.
async function deletarAtendimento(id) {
    return await apiFetch(`/atendimentos/${id}`, {
        method: 'DELETE'
    });
}
