(function() {
    Api.get("/friends-requests")
        .then(res => console.log(res.json()));
})();
