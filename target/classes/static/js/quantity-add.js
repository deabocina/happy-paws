document.querySelectorAll('.quantity-input').forEach(input => {
    if (!input.value || input.value === null) {
        input.value = 1;
    }

    input.addEventListener('change', function() {
        const productId = this.getAttribute('id').split('-')[1];
        const newQuantity = this.value;
        updateQuantity(productId, newQuantity);
    });
});


function updateQuantity(productId, newQuantity) {
    fetch('/shopping-bag', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `productId=${productId}&quantity=${newQuantity}`
    })
    .then(response => {
        if (response.ok) {
            console.log('Quantity updated successfully.'); //
        } else {
            throw new Error('Network response was not ok.');
        }
    })
    .catch(error => {
        console.error('There was a problem updating quantity:', error);
    });
}
