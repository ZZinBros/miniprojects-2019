(function() {
    function onModifyClick() {
        const baseUrl = document.location.origin;
        const contents = document.getElementById('post-modify-content');
        const url = baseUrl + "/posts/" + contents.dataset.postid;
        const api = new AjaxApi();
        api.put(url, { "contents": contents.value })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                } else if (res.ok) {
                    window.location.href = baseUrl;
                }
            })
    }
    document.getElementById("post-edit-btn").addEventListener("click", onModifyClick);
})();