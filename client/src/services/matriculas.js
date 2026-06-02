async function realizarMatricula(dadosMatricula) {
    return await apiFetch('/matriculas', {
        method: 'POST',
        body: JSON.stringify(dadosMatricula)
    });
}