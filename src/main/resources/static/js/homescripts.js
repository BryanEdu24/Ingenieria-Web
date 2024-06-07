"use strict"

function changeTaskState(idTask) {

    var params = {
        idTask: idTask
    };

    go("/user/changeTaskState", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function payExpense(userExpenseId) {
    var params = {
        userExpenseId: userExpenseId
    };

    go("/user/payExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#expenseHomeCard" + userExpenseId).hide()

            $("#divCurrentBalanceHome").empty()

            if (d >= 0) {
                $("#divCurrentBalanceHome").append(`
                <h3 id="currentBalanceHome" class="text-success">${d}</h3>
                &euro;
            `)
            } else {
                $("#divCurrentBalanceHome").append(`
                <h3 id="currentBalanceHome" class="text-danger">${d}</h3>
                &euro;
                `)
            }

        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });

}