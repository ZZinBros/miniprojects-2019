(function () {
    const baseUrl = document.location.origin;

    function onSharingClick(event) {
        const contents = event.target.closest('.modal-content').querySelector('textarea');
        const postId = contents.dataset.postid;
        const url = baseUrl + "/posts/share";

        Api.post(url, {"contents": contents.value, "sharedPostId": postId})
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                } else if (res.ok) {
                    alert("게시글이 공유되었습니다.");
                    window.location.href = baseUrl;
                }
            })
    }

    const shares = document.getElementsByClassName("post-share");
    Array.from(shares)
        .map(element => element.addEventListener("click", onSharingClick));
})();