(function() {
    const sendFriendRequest = (event)=>{
        const requestFriendId = event.target.dataset.userId;
        const api = new AjaxApi();
        api.post("/friends", {requestFriendId})
            .then(res => {
                if(res.redirected) {
                    window.location.href = res.url;
                }
            });
    };
    document.getElementById("add-friend-btn").addEventListener("click", sendFriendRequest);
})();
