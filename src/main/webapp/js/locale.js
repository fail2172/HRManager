function setLocale() {
    const selectedValue = document.getElementById("list").value;
    document.cookie = "lang=" + selectedValue;
    location.reload();
}