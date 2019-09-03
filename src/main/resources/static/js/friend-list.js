(function () {
    const requestScrollBar = document.getElementById('requests-scroll-bar');
    const friendRequestNotification = document.getElementById('friend-requests-notification');
    const friendsScrollBar = document.getElementById('friends-scroll-bar');

    const addFriend = (scrollbar, friend) => {
        const li = `<li>
                        <a href="/posts?author=${friend.id}">
                            <img src="${friend.profile}" />
                            <span>${friend.name}</span>
                        </a>
                    </li>`;

        scrollbar.insertAdjacentHTML('beforeend', li);
    };

    const handleFriendRequest = (event) => {
        const postFriendRequest = () => {
            Api.post('/friends', {requestFriendId})
                .then(res => {
                    if (res.redirected) {
                        window.location.reload();
                    }
                });
        };

        const deleteFriendRequest = () => {
            Api.delete('/friends-requests/' + requestFriendId)
                .then(res => {
                    if (res.ok) {
                        window.location.reload();
                    }
                });
        };

        const target = event.target;
        const requestFriendId = event.target.closest('li').value;

        if (target.classList.contains('friend-request-add')) {
            postFriendRequest();
            return;
        }
        deleteFriendRequest();
    };

    const addFriendRequest = (element, request) => {
        const li = `<li value=${request.id}>
                        <img src="${request.profile}" />
                        <span>${request.name}</span>
                        <button class="friend-request-add">추가</button>
                        <button class="friend-request-reject">거절</button>
                      </li>`;
        element.insertAdjacentHTML('beforeend', li);
    };

    const notifyFriendRequest = (array) => {
        if (array.length > 0) {
            const span = `<span class="dot mrg-vertical-10 mrg-horizon-15"></span>`;
            friendRequestNotification.insertAdjacentHTML('beforeend', span);
        }
    };

    const addElementsToScrollBar = (scrollbar, url, addElement, checkNotification) => {
        Api.get(url)
            .then(res => res.json())
            .then(array => {
                array.forEach((json) => addElement(scrollbar, json));
                checkNotification(array);
            });
    };

    const init = () => {
        if (requestScrollBar) {
            addElementsToScrollBar(requestScrollBar, '/friends-requests', addFriendRequest, notifyFriendRequest);
            requestScrollBar.addEventListener('click', handleFriendRequest);
            addElementsToScrollBar(friendsScrollBar, '/friends', addFriend, () => {
            });
        }
    }

    init();
})();
