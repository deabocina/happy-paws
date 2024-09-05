document.addEventListener('DOMContentLoaded', function () {
    const productRows = document.querySelectorAll('.product-table tbody tr');
    const deliveryOption = document.getElementById('deliveryOption');
    const totalOrder = document.getElementById('total-order');
    const productPriceDisplay = document.getElementById('product-price');
    const deliveryPriceDisplay = document.getElementById('delivery-price');


    let totalPrice = 0;
    productRows.forEach(row => {
        const priceCell = row.querySelector('td:nth-child(2)');
        const priceText = priceCell.textContent.trim().replace('€', '');
        const price = parseFloat(priceText);

        const quantityCell = row.querySelector('td:nth-child(3)');
        const quantity = parseInt(quantityCell.textContent.trim());

        totalPrice += price * quantity;
    });

    // Set total product price
    productPriceDisplay.textContent = totalPrice.toFixed(2) + ' €';

    let deliveryPrice = 0;
    if (totalPrice >= 35) {
        deliveryPrice = 0;
    } else {
        deliveryPrice = 4;
    }

    deliveryPriceDisplay.textContent = deliveryPrice.toFixed(2) + ' €';
    const total = totalPrice + deliveryPrice;

    // Set total order price in PayPal form
    document.querySelector('input[name="amount"]').value = total.toFixed(2);
    // Set total order price in HTML
    totalOrder.textContent = total.toFixed(2) + ' €';
});
