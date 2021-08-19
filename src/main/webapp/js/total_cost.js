$(document).ready(calculate);
$(document).on("keyup", calculate);

function calculate() {
    var sum = 0;
    var mealNumber = document.getElementById('meal_number').value;
    var price = document.getElementById('total_price').value;
    $(mealNumber).each(function(){
        sum += price;
    });
    $("#total_price").html(sum);
}

// function calculateArea() {
//     var width = document.getElementById('width').value;
//     var height = document.getElementById('height').value;
//     var total = document.getElementById('product');
//     var myResult = width * height;
//     total.value = myResult;
// }