(function() {
    function onAddPostClick() {
        const contents = document.getElementById('post-content').value;
        const url = document.location.href;
        const api = new AjaxApi();
        api.post(`${url}/posts`, { contents })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url
                } else if (res.ok) {
                    window.location.reload();
                }
            })
    }

    function handleFiles(files) {
        const preview = document.getElementById("feed-image-preview");
        preview.style.visibility = "visible";
        const file = this.files[0];
        if (!file.type.startsWith('image/')) { return; }
        preview.classList.add("obj");
        preview.file = file;

        const reader = new FileReader();
        reader.onload = (function(aImg) {
            return function(e) {
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
        const form = document.getElementById("feed-add-form")
        form.submit();
    });

    document.getElementById("feed-image").addEventListener("change", handleFiles);

    document.getElementById("feed-add-with-image-btn").addEventListener("click", () => {
        $("#feed-add-modal").modal();
    });

    document.getElementById("feed-add-btn").addEventListener("click", onAddPostClick);
})();