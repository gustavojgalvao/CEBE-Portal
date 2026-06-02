async function listarCursos() {
    return await apiFetch('/cursos');  // GET /cursos
}
async function listarTurmas() {
    return await apiFetch('/turmas'); // GET /turmas
}