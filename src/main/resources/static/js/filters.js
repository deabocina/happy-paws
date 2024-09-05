document.addEventListener('DOMContentLoaded', function() {
        const categoryDropdown = document.getElementById('category');
        const subcategoryDropdown = document.getElementById('subcategory');

        categoryDropdown.addEventListener('change', function() {
            const selectedCategory = categoryDropdown.value;
            // Clear existing options
            subcategoryDropdown.innerHTML = '';

            if (selectedCategory === 'Food') {
                const foodSubcategories = ['All', 'Dry food', 'Wet food'];
                foodSubcategories.forEach(function(subcategory) {
                    const option = document.createElement('option');
                    option.value = subcategory;
                    option.text = subcategory;
                    subcategoryDropdown.appendChild(option);
                });
            } else if (selectedCategory === 'Treats') {
                const treatsSubcategories = ['All', 'Regular treats', 'Dental treats'];
                treatsSubcategories.forEach(function(subcategory) {
                    const option = document.createElement('option');
                    option.value = subcategory;
                    option.text = subcategory;
                    subcategoryDropdown.appendChild(option);
                });
            }
        });
    });