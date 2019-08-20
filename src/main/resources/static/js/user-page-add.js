(function() {
    function onAddPostClick() {
        const contents = document.getElementById('post-content').value;
        const api = new AjaxApi();
        api.post(`/posts`, { contents })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url
                } else if (res.ok) {
                    window.location.reload();
                }
            })
    }
    document.getElementById("feed-add-btn").addEventListener("click", onAddPostClick);
})();