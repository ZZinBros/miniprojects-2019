(function () {
    function onLikeClick(event) {
        const baseUrl = document.location.origin;
        const postId = event.target.dataset.postid;
        const url = baseUrl + "/posts/" + postId + "/like";
        const api = new AjaxApi();
        api.put(url, {})
            .then(res => {
                return res.json()
            })
            .then(json => {
                event.target.closest("ul").parentElement.querySelector(".countOfLike").innerHTML = json;
            })
    }

    const likes = document.getElementsByClassName("like");
    for (let i = 0; i < likes.length; i++) {
        likes[i].addEventListener("click", (event) => onLikeClick(event));
    }
})();