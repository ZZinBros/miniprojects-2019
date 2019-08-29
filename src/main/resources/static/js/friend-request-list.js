(function() {
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
            .then(array => array.forEach((json) => addFriendRequest(element, json)));
    };

    const scrollBar = document.getElementById('requests-scroll-bar');
    if (scrollBar !== null) {
        addFriendRequests(scrollBar);
    }
})();