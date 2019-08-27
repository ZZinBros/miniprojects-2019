(function() {
    const handleLogout = (event) => {
        Api.post("/logout", {})
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                }
            });
    };

    document.getElementById('logout-anchor').addEventListener('click', handleLogout);
})();
