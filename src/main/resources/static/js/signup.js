(function() {
    const pwd = document.getElementById("password");
    const pwdConfirm = document.getElementById("passwordConfirm");
    const inputPasswordHandler = () => {
        pwdConfirm.style.background = "#ffd5e3";
        if (pwd.value === pwdConfirm.value) {
            pwdConfirm.style.background = "white";
        }
    }

    pwdConfirm.addEventListener('input', inputPasswordHandler);
})();