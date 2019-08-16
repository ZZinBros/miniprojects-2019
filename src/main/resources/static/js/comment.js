'use strict'

const createdComment = {
    contents: 'test contents',
    author: 'test author'
}

const COMMENTAPP = (function () {
    const commentTemplate = function (comment) {
        return `<li class="comment-item">
                <img class="thumb-img img-circle" src="images/default/eastjun_profile.jpg" alt="">
                <div class="info">
                    <div class="bg-lightgray border-radius-18 padding-10 max-width-100">
                        <a href="" class="title text-bold inline-block text-link-color">${comment.author}</a>
                        <span>${comment.contents}</span>
                    </div>
                    <div class="font-size-12 pdd-left-10 pdd-top-5">
                        <span class="pointer text-link-color">좋아요</span>
                        <span>·</span>
                        <span class="pointer text-link-color">답글 달기</span>
                        <span>·</span>
                        <span class="pointer">2시간</span>
                    </div>
                </div>
            </li>`
    }

    const commentAddBtns = document.getElementsByClassName('comment-add-btn');

    const CommentController = function () {
        const commentService = new CommentService()

        const addComment = function () {
            let commentAddBtn;
            for (commentAddBtn of commentAddBtns) {
                commentAddBtn.addEventListener('click', commentService.add);
            }
        }

        const init = function () {
            addComment()
        }

        return {
            init: init
        }
    }

    const CommentService = function () {
        const add = function (event) {
            event.stopPropagation();

            let commentTextArea = event.target.previousElementSibling;
            let commentContents = commentTextArea.value;
            let parentPost = event.target.parentElement.parentElement.parentElement.parentElement;
            let postId = parentPost.getAttribute('data-post-id');

            console.log(commentContents);
            fetch(`/comments`, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    postId: postId,
                    contents: commentContents
                })
            }).then(res => res.json())
                .then(createdComment => {
                    let comments = event.target.parentElement.previousElementSibling;
                    comments.insertAdjacentHTML('beforeend', commentTemplate(createdComment))
                });
        }

        return {
            add: add,
        }
    }

    const init = function () {
        const commentController = new CommentController()
        commentController.init()
    };

    return {
        init: init,
    };
})();

COMMENTAPP.init();
