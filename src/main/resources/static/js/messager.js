const MessageAdder = function(position) {

    const add = (element, message) => {
        element.innerText = '';

        const errorMsg =
            `<div class="alert alert-primary" role="alert">
                  ${message}
             </div>`;
        element.insertAdjacentHTML(position, errorMsg);
    }

    return {
        add
    };
};