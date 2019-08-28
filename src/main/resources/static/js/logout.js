(function () {
        const handleLogout = (event) => {
            Api.post("/logout", {})
                .then(res => {
                    if (res.redirected) {
                        window.location.href = res.url;
                    }
                });
        };

        const logoutAnchor = document.getElementById('logout-anchor');
        if (logoutAnchor !== null) {
            logoutAnchor.addEventListener('click', handleLogout);
        }
    }
)();
