"use strict"

function newExpense(event) {
    event.preventDefault();

    let params = {
        description: $("#expenseDescription").val(),
        quantity: $("#expenseQuantity").val()
    };
    console.log(`PARAMS: description:${params.description}, quantity:${params.quantity}`);

    go("/user/newExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}
