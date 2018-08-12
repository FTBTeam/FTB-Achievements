$(function () {
    setInterval(updateTime, 250);
    setInterval(updateData, 5000);
});

function updateTime() {
    $("#time").load("time");
    console.log("Time");
}

function updateData() {
    $("#data").load("data");
    console.log("Data");
}