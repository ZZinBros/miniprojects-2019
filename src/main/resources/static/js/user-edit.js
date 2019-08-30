(function () {
    const editBtn = document.getElementById("user-edit-btn");
    const nameInput = document.getElementById('user-edit-name');
    const emailInput = document.getElementById('user-edit-email');
    const loginNameSpan = document.querySelector('.user-info span');
    const messagePart = document.querySelector('#user-edit-message');
    const messageAdder = new MessageAdder('beforeend');

    const updateBtnHandler = (event) => {
        event.preventDefault();

        const userId = window.location.href.split("/")[4];
        const name = nameInput.value;
        const email = emailInput.value;
        Api.put(`/users/${userId}`, {name, email})
            .then(res => {
                if (res.status === 200) {
                    res.json()
                        .then(json => {
                            const object = json.object;
                            nameInput.value = object.name;
                            emailInput.value = object.email;
                            loginNameSpan.innerText = object.name;
                            messageAdder.add(messagePart, json.message);
                        });
                }
                if (res.status === 202) {
                    res.json()
                        .then(json => {
                            messageAdder.add(messagePart, json.message);
                        });
                }
            });
    };

    editBtn.addEventListener("click", updateBtnHandler);
})();