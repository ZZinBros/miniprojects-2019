(function () {
    const requestScrollBar = document.getElementById('requests-scroll-bar');
    const friendRequestNotification = document.getElementById('friend-requests-notification');
    const friendsScrollBar = document.getElementById('friends-scroll-bar');

    const addFriendRequest = (element, req) => {

        const handleSubmit = (event) => {
            const requestFriendId = event.target.parentElement.value;
            Api.post('/friends', {requestFriendId})
                .then(res => {
                    if (res.redirected) {
                        window.location.reload();
                    }
                });
        };

        const li = document.createElement('li');
        li.value = req.id;

        const span = document.createElement('span');
        span.innerText = req.name;

        li.insertAdjacentElement('afterbegin', span);

        const submitBtn = document.createElement('button');
        submitBtn.innerText = '추가';
        submitBtn.addEventListener('click', handleSubmit);

        li.insertAdjacentElement('beforeend', submitBtn);

        element.insertAdjacentElement('beforeend', li);
    };

    const notifyFriendRequest = (array) => {
        if (array.length > 0) {
            const span = document.createElement('span');
            span.classList.add('dot', 'mrg-vertical-10', 'mrg-horizon-15');
            friendRequestNotification.insertAdjacentElement('beforeend', span);
        }
    };

    const addFriend = (scrollbar, json) => {
        const li = document.createElement('li');
        const anchor = document.createElement('a');
        anchor.href = '/posts?author=' + json.id;

        const span = document.createElement('span');
        span.innerText = json.name;
        anchor.insertAdjacentElement('beforeend', span);

        li.insertAdjacentElement('afterbegin', anchor);

        scrollbar.insertAdjacentElement('beforeend', li);
    };

    const addElementsToScrollBar = (scrollbar, url, addElement, checkNotification) => {
        Api.get(url)
            .then(res => res.json())
            .then(array => {
                array.forEach((json) => addElement(scrollbar, json));
                checkNotification(array);
            });
    };

    if (requestScrollBar !== null) {
        addElementsToScrollBar(requestScrollBar, '/friends-requests', addFriendRequest, notifyFriendRequest);
        addElementsToScrollBar(friendsScrollBar, '/friends', addFriend, () => {});
    }
})();