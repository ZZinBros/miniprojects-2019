'use strict';

const CommentApp = (function () {
    const commentTemplate = function (comment) {
        return `<li class="comment-item">
                <img class="thumb-img img-circle" src="images/default/eastjun_profile.jpg" alt="">
                <div class="info">
                    <div class="bg-lightgray border-radius-18 padding-10 max-width-100">
                        <a href="" class="title text-bold inline-block text-link-color">${comment.authorName}</a>
                        <span>${comment.contents}</span>
                    </div>
                    <div class="font-size-12 pdd-left-10 pdd-top-5">
                        <span class="pointer text-link-color">좋아요</span>
                        <span>·</span>
                        <span class="pointer text-link-color">답글 달기</span>
                        <span>·</span>
                        <span class="pointer">${comment.createdDateTime}</span>
                    </div>
                </div>
            </li>`
    };

    const CommentController = function () {
        const commentService = new CommentService();

        const addComment = function () {
            const commentAddBtns = document.getElementsByClassName('comment-add-btn');

            Array.from(commentAddBtns)
                .map(commentAddBtn => commentAddBtn.addEventListener('click', commentService.add));
        };

        const init = function () {
            addComment()
        };

        return {
            init: init
        }
    };

    const CommentService = function () {
        const add = function (event) {
            event.stopPropagation();

            const parentPost = event.target.closest(".card");
            const postId = parentPost.dataset.postId;
            const commentArea = event.target.closest(".add-comment");
            const commentTextArea = commentArea.querySelector("textarea");
            const contents = commentTextArea.value;

            Api.post(`/comments`, {
                postId,
                contents
            }).then(res => res.json())
                .then(createdComment => {
                    let comments = event.target.closest(".comment").querySelector(".comment-items");
                    comments.insertAdjacentHTML('beforeend', commentTemplate(createdComment));
                });
        };

        return {
            add
        }
    };

    const init = function () {
        const commentController = new CommentController();
        commentController.init();
    };

    return {
        init
    };
})();

CommentApp.init();
