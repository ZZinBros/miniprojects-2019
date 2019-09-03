(function () {
    const searchScrollBar = document.getElementById('search-dropdown-menu');
    const pageNumber = 0;
    const pageSize = 20;

    const cleanInnerText = (element) => {
        element.innerHTML = '';
    };

    const addSearchResult = (result) => {
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

        if (searchValue === '') {
            event.preventDefault();
            cleanInnerText(searchScrollBar);
            return;
        }

        Api.get("/users?name=" + searchValue + "&page=" + pageNumber +"&size=" + pageSize)
            .then(res => {
                cleanInnerText(searchScrollBar);
                return res.json();
            })
            .then(json => {
                json.forEach(addSearchResult);
            })
            .catch(console.log);
    };

    const searchNameInput = document.getElementById('input-search-name');
    searchNameInput.addEventListener('keyup', handleInputEvent);
})();