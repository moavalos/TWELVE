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
function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function(){
        const output = document.getElementById('profileImage');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}