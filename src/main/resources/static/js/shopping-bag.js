document.addEventListener('DOMContentLoaded', function () {
    const quantityInputs = document.querySelectorAll('.quantity-input');

    quantityInputs.forEach(function (input, index) {
        input.addEventListener('change', function () {
            updateTotal();
        });
    });

    function updateTotal() {
        let total = 0;
        document.querySelectorAll('.shopping-bag-products tbody tr').forEach(function (row) {
            const price = parseFloat(row.querySelector('td:nth-child(3)').textContent.replace('€', ''));
            const quantity = parseInt(row.querySelector('.quantity-input').value);
            const rowTotal = price * quantity;
            total += rowTotal;
            row.querySelector('td.products-total').textContent = rowTotal.toFixed(2) + ' €';
        });

        // Update total price in second table
        const totalElements = document.querySelectorAll('.shopping-bag-total tbody tr td:first-child');
        totalElements.forEach(function (element) {
            element.textContent = total.toFixed(2) + ' €';
        });

        const shippingCost = document.getElementById('shipping-cost');
        const copiedTotal = total.toFixed(2);
        if (copiedTotal >= 35) {
            shippingCost.textContent = '0.00 €';
        } else {
            shippingCost.textContent = '4.00 €';
        }

        const totalOrder = document.getElementById('total-order');
        const totalWithShipping = total + (copiedTotal >= 35 ? 0 : 4);
        totalOrder.textContent = totalWithShipping.toFixed(2) + ' €';

        const copiedTotalElement = document.getElementById('copied-total');
        copiedTotalElement.textContent = copiedTotal + ' €';
    }

    updateTotal();
});
