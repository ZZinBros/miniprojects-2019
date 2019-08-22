(function () {
    const baseUrl = document.location.origin;
    const api = new AjaxApi();

    function onLikeClick(event) {
        if (event.target.tagName !== "LI") {
            return event.target.parentElement.click();
        }
        const postId = event.target.dataset.postid;
        const url = baseUrl + "/posts/" + postId + "/like";

        api.put(url, {})
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                    reject();
                }
                return res.json()
            })
            .then(countOfLike => {
                event.target
                    .closest("ul").parentElement
                    .querySelector(".countOfLike")
                    .innerText = countOfLike;
            })
    }

    const likes = document.getElementsByClassName("like");
    Array.from(likes)
        .map(element => element.addEventListener("click", onLikeClick));
})();