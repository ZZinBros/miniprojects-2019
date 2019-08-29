(function() {
    const sendFriendRequest = (event) => {
        const requestFriendId = event.target.dataset.userId;
        Api.post("/friends", { requestFriendId })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                }
            });
    };

    const friendAddBtn = document.getElementById("add-friend-btn");
    if (friendAddBtn) {
        friendAddBtn.addEventListener('click', sendFriendRequest);
    }
})();
