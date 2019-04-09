function AddTaFormfun() {
    let selectedBranch = document.getElementById("TA_Branch").val();
    let x = document.getElementById(selectedBranch + "TA");
    document.getElementById("B.TechTA").style.display = "none";
    document.getElementById("M.TechTA").style.display = "none";
    document.getElementById("PhDTA").style.display = "none";
    if (x.style.display === "none") {
        x.style.display = "block";
    }
}
