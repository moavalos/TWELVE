//Historial calendario

function toggleDates(datesId) {
    var dates = document.getElementById(datesId);
    if (dates.style.display === "none" || dates.style.display === "") {
        dates.style.display = "block";
    } else {
        dates.style.display = "none";
    }
}

//////////////////////////////////////
