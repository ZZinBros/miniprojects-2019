(function() {
    const deleteBtn = document.getElementById("user-delete-btn");

    function deleteBtnHandler(event) {
        event.preventDefault();

        const id = window.location.href.split("/")[4];
        Api.delete(`${window.location.origin}/users/${id}`)
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                }
            })
    }

    deleteBtn.addEventListener("click", deleteBtnHandler)
})();