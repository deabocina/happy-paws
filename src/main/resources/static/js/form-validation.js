function calculateAge(dob) {
    const today = new Date();
    const birthDate = new Date(dob);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

function validateForm() {
    const form = document.querySelector('.login__form');
    const dobInput = form.elements['dateOfBirth'];
    const emailInput = form.elements['email'];
//    const phoneNumberInput = form.elements['phoneNumber'];

        dobInput.addEventListener('change', function () {
        const age = calculateAge(this.value);
        if (age < 18) {
            alert('You must be at least 18 years old to register.');
            this.value = ''; // Clear the date input
        }
    });

    form.addEventListener('submit', function (event) {
        if (!validateEmail(emailInput.value)) {
            alert('Please enter a valid email address.');
            event.preventDefault();
        }

//        if (!validatePhoneNumber(phoneNumberInput.value)) {
//            alert('Please enter a valid phone number (8 to 15 digits).');
//            event.preventDefault();
//        }
    });
}

function validatePassword() {
    var password = document.querySelector('input[name="password"]');
    var confirmPassword = document.querySelector('input[name="confirmPassword"]');
    var submitButton = document.querySelector('input[type="submit"]');

    function validate() {
        if (password.value !== confirmPassword.value) {
            confirmPassword.setCustomValidity("Passwords don't match");
        } else {
            confirmPassword.setCustomValidity('');
        }
    }

    password.addEventListener('change', validate);
    confirmPassword.addEventListener('keyup', validate);
    confirmPassword.addEventListener('change', validate);

    submitButton.addEventListener('click', function(event) {
        if (password.value !== confirmPassword.value) {
            event.preventDefault();
        }
    });
}

function sortCities() {
    var select = document.querySelector('select[name="city"]');
    var options = Array.from(select.options);
    options.sort(function(a, b) {
        return a.text.localeCompare(b.text, 'en', { sensitivity: 'accent' });
    });
    select.innerHTML = '';
    options.forEach(function(option) {
        select.add(option);
    });
}

function initialize() {
    sortCities();
    validateForm();
    validatePassword();
}

window.onload = function() {
    initialize();
};

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

//function validatePhoneNumber(phoneNumber) {
//    const phoneRegex = /^\d{8,15}$/;
//    return phoneRegex.test(phoneNumber);
//}

function showConfirmation() {
//    const phoneNumberInput = document.querySelector('input[name="phoneNumber"]');
//    const phoneNumber = phoneNumberInput.value;
//
//    if (!validatePhoneNumber(phoneNumber)) {
//        alert('Please enter a valid phone number with 8 to 15 digits.');
//        return false;
//    }

    const password = document.querySelector('input[name="password"]').value;
    const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match. Please re-enter matching passwords.');
        return false;
    }

    if (password.length < 8 || password.length > 20) {
        alert('Password must be between 8 and 20 characters.');
        return false;
    }

    const age = document.querySelector('input[name="age"]').value;
    if (age < 18) {
         alert('You must be at least 18 years old to register.');
         return false;
    }

    alert("Your account has been successfully registered!");
    return true;
}