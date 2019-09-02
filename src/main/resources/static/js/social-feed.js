(function () {
    const onAddPostClick = () => {
        const contents = document.getElementById('post-content').value;
        const displayStrategyText = document.getElementsByClassName('ti-check')[0].previousElementSibling.textContent;

        let displayStrategy;
        switch (displayStrategyText) {
            case '전체 공개':
                displayStrategy = 1;
                break;
            case '친구만':
                displayStrategy = 2;
                break;
            case '나만 보기':
                displayStrategy = 3;
                break;
        }

        const url = document.location.href;
        Api.post(`${url}/posts`, { "contents": contents, "displayStrategy": displayStrategy })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url
                } else if (res.ok) {
                    window.location.reload();
                }
            })
    };

    const onDisplayStrategyHilight = (event) => {
        event.target.closest('ul').querySelector('.ti-check').classList.remove('ti-check');
        event.target.closest('li').querySelectorAll('span')[1].classList.add('ti-check');
        document.getElementById('feed-add-display-btn').querySelector('span').innerText
            = event.target.closest('li').querySelector('span').textContent;
    };

    function handleFiles(files) {
        const preview = document.getElementById("feed-image-preview");
        preview.style.visibility = "visible";
        const file = this.files[0];
        console.log(file.size);
        if (!file.type.startsWith('image/')) {
            alert('이미지 파일만 등록할 수 있습니다.');
            this.value = "";
            $("#feed-add-modal").modal("hide");
            return;
        }
        if (file.size >= 10000000) {
            alert('10MB 이하의 파일만 등록할 수 있습니다.');
            this.value = "";
            $("#feed-add-modal").modal("hide");
            return;
        }
        preview.classList.add("obj");
        preview.file = file;

        const reader = new FileReader();
        reader.onload = (aImg => {
            return function (e) {
                aImg.src = e.target.result;
            };
        })(preview);
        reader.readAsDataURL(file);
    }

    document.getElementById("feed-add-image-btn").addEventListener("click", (e) => {
        e.preventDefault();
        document.getElementById("feed-image").click();
    });

    document.getElementById("feed-submit-btn").addEventListener("click", (e) => {
        e.preventDefault();
        document.getElementById("feed-add-form").submit();
    });

    document.getElementById("feed-image").addEventListener("change", handleFiles);

    document.getElementById("feed-add-with-image-btn").addEventListener("click", () => {
        $("#feed-add-modal").modal();
    });

    document.getElementById("feed-add-btn").addEventListener("click", onAddPostClick);

    document.getElementById('post-display-dropdown').addEventListener("click", e => onDisplayStrategyHilight(e))
})();