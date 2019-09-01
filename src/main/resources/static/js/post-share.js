(function () {
    const baseUrl = document.location.origin;

    function onSharingClick(event) {
        const contents = event.target.closest('.modal-content').querySelector('textarea');
        const postId = contents.dataset.postid;
        const displayStrategyText = event.target.closest('ul').querySelector('.ti-check').previousElementSibling.textContent;

        const url = baseUrl + "/posts/shared";

        let displayStrategy;
        switch (displayStrategyText) {
            case '전체 공개':
                displayStrategy = 1;
                break;
            case '친구만':
                displayStrategy = 2;
                break;
            case '나만 보기':
                displayStrategy = 3;
                break;
        }

        Api.post(url, {"contents": contents.value, "sharedPostId": postId, "displayStrategy": displayStrategy})
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