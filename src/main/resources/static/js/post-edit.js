(function() {
    function onModifyClick(event) {
        const baseUrl = document.location.origin;
        const contents = event.target.closest('.modal-content').querySelector('textarea');
        const url = baseUrl + "/posts/" + contents.dataset.postid;
        Api.put(url, { "contents": contents.value })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                } else if (res.ok) {
                    window.location.href = baseUrl;
                }
            })
    }
    const postEdits = document.getElementsByClassName("post-edit");
    Array.from(postEdits)
        .map(element => element.addEventListener("click", onModifyClick));
})();