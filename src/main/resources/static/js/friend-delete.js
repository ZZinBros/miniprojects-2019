(function() {
    const deleteFriend = (event) => {
        const deleteId = event.target.closest('button').dataset.userId;
        Api.delete("/friends/" + deleteId)
            .then(res => {
                if (res.ok) {
                    window.location.reload();
                }
            });
    };

    const deleteBtn = document.getElementById("friend-btn");
    if (deleteBtn) {
        deleteBtn.addEventListener("click", deleteFriend);
    }
})();
