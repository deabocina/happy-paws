document.getElementById("back-to-top").addEventListener("click", function() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
});

function sortProducts() {
    const sortOrder = $('#sort-order').val();
    const products = $('.product');

    products.sort(function(a, b) {
        const priceA = parseFloat($(a).find('#product-price').text());
        const priceB = parseFloat($(b).find('#product-price').text());

        if (sortOrder === 'asc') {
            return priceA - priceB;
        } else {
            return priceB - priceA;
        }
    });

    $('.product-row').empty().append(products);
}

