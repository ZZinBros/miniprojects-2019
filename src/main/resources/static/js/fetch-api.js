const AjaxApi = function() {
    this.bodyData = function(method, data) {
        this.method = method;
        this.headers = {
            'Content-Type': 'application/json'
        };
        this.body = JSON.stringify(data);
    };

    this.post = function(url, data) {
        return fetch(url, new this.bodyData("POST", data));
    };

    this.put = function(url, data) {
        return fetch(url, new this.bodyData("PUT", data));
    };

    this.delete = function(url) {
        return fetch(url, new this.bodyData("DELETE"));
    };

    this.get = function(url) {
        return fetch(url, new this.bodyData("GET"));
    };
}