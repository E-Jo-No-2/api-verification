function toggleDropdown() {
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
}

function selectTheme(value, label) {
    const selectedTheme = document.getElementById("selectedTheme");
    selectedTheme.textContent = label;

    // 기존 테마 필터링 함수 호출
    filterByTheme(value);

    // 드롭다운 닫기
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = "none";
}

// 드롭다운 외부 클릭 시 닫기
window.addEventListener("click", function(event) {
    if (!event.target.closest(".dropdown-container")) {
        const dropdownMenu = document.getElementById("dropdownMenu");
        dropdownMenu.style.display = "none";
    }
});
