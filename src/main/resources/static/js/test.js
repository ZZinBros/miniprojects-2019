(function () {
    console.log("test 시작 must delete");
    const searchScrollBar = document.getElementById('search-dropdown-menu');

    const cleanInnerText = (element) => {
        element.innerText = '';
    };

    const addSearchResult = (result) => {
        console.log(result);
        const element =
        `<li>
            <a href="/posts?author=${result.id}" class="pointer dropdown-item">
                <img src="${result.profile}" />
                <span class="">${result.name}</span>
            </a>
        </li>`;

        searchScrollBar.insertAdjacentHTML('afterbegin', element);
    };

    const handleInputEvent = (event) => {
        const searchValue = event.target.value;
        console.log("Input Event 시작 must delete");
        console.log("input 값 : " + searchValue);
        if (searchValue === '') {
            event.preventDefault();
            return;
        }

        Api.get("/users?name=" + searchValue)
            .then(res => {
                console.log(res);
                cleanInnerText(searchScrollBar);
                return res.json();
            })
            .then(json => {
                json.forEach(addSearchResult);
            });
    };

    const searchNameInput = document.getElementById('input-search-name');
    searchNameInput.addEventListener('input', handleInputEvent);
})();