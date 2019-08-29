(function() {
    const scrollBar = document.getElementById('requests-scroll-bar');
    const notificationLabel = document.getElementById('friend-requests-notification');

    const handleSubmit = (event) => {
        const requestFriendId = event.target.parentElement.value;
        Api.post('/friends', {requestFriendId})
            .then(res => {
                if(res.redirected) {
                    window.location.reload();
                }
            });
    };

    const addFriendRequest = (element, req) => {
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

    const addFriendRequests = (element) => {
        Api.get('/friends-requests')
            .then(res => res.json())
            .then(array => {
                if (array.length > 0) {
                    array.forEach((json) => addFriendRequest(element, json));
                    const span = document.createElement('span');
                    span.classList.add('dot', 'mrg-vertical-10', 'mrg-horizon-15');
                    notificationLabel.insertAdjacentElement('beforeend', span);
                }
            });
    };

    if (scrollBar !== null) {
        addFriendRequests(scrollBar);
    }
})();