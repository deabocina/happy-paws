document.addEventListener('DOMContentLoaded', function() {
    const quantityInputs = document.querySelectorAll('.quantity-input');

    quantityInputs.forEach(function(input) {
        input.addEventListener('change', function() {
            const maxValue = parseInt(this.getAttribute('max'));
            const enteredValue = parseInt(this.value);

            if (enteredValue > maxValue) {
                this.value = maxValue;
            }
        });
    });
});