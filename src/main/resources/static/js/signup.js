(function() {
    const pwd = document.getElementById("signup-password");
    const pwdConfirm = document.getElementById("signup-password-confirm");
    const inputPasswordHandler = () => {
        pwdConfirm.style.background = "#ffd5e3";
        if (pwd.value === pwdConfirm.value) {
            pwdConfirm.style.background = "white";
        }
    }

    pwdConfirm.addEventListener('input', inputPasswordHandler);
})();