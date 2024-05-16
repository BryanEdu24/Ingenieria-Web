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