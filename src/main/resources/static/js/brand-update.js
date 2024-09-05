function toggleBrandNameInput() {
        const selectedBrandId = document.getElementById("selectedBrandId").value;
        const brandNameInput = document.getElementById("brandName");
        brandNameInput.disabled = !selectedBrandId;
        brandNameInput.value = '';
}