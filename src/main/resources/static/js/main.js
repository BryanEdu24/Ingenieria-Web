"use strict"

function newTask(event) {
    event.preventDefault();

    let params = {
        title: $("#taskTitle").val(),
        user_id: $("#addTaskSelectUser").val(),
        room_id: $("#addTaskSelectRoom").val()
    };
    console.log(`PARAMS: title:${params.title}, user_id:${params.user_id}, room_id:${params.room_id}`);
    go("/user/newTask", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#form-newTask").reset();

            //TODO
            /*
            $("#divCardsTasks").append(
            `<div class="card-content d-flex p-1 bg align-items-center">
            <div class="col">
                <h3 th:text="${d.title}">Hacer la comida</h3>
                <h5 th:text="${d.author}">Lucas</h5>
            </div>
            <div class="col">
                <div> 
                    <h5 th:text="${d.creationDate}">10/02/2024<h5>
                </div>
                <div>
                    <h5 th:text="${d.room.name}">Cocina</h5>
                </div>
            </div>
            <button class="btn">
                <img th:src="@{/img/vista.png}" src="/img/vista.png" alt="Imagen" width="50"
                    height="50" style="margin-right: 5%;">
            </button>
            </div>`)*/

        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}