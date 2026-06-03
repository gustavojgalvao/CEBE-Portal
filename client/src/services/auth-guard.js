(function () {
    const token = localStorage.getItem('cebe_token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }
})();