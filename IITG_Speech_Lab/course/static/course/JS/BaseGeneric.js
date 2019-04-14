function AddTaFormfun() {
    let selectedBranch = document.getElementById("TA_BRANCH").value;
    let x = document.getElementById(selectedBranch + "TA");
    document.getElementById("BTechTA").style.display = "none";
    document.getElementById("MTechTA").style.display = "none";
    document.getElementById("PhDTA").style.display = "none";
    if (x.style.display === "none") {
        x.style.display = "block";
    }
    console.log(5);
}


