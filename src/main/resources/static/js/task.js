"use strict"

function newTask(event) {
    event.preventDefault();

    let params = {
        title: $("#taskTitle").val(),
        user_id: $("#addTaskSelectUser").val(),
        room_id: $("#addTaskSelectRoom").val(),
        room_name:$("#addTaskSelectRoom").textContent()
    };
    console.log(`PARAMS: title:${params.title}, user_id:${params.user_id}, room_id:${params.room_id}`);
    go("/user/newTask", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTasks").hide()

            $("#form-newTask")[0].reset()

            const formattedDate = formatDate(d.creationDate);

            //TODO habitación
            $('#divCardsTasks').prepend(
                `<div class="card my-2">
                    <div class="card-content d-flex p-1 align-items-center taskCard">
                        <div class="col ms-2 my-1">
                            <h3>${d.title}</h3>
                            <h5>${d.author}</h5>
                        </div>
                        
                        <div class="col-4">
                            <div>
                                <h5>${formattedDate}</h5>
                            </div>
                            <div>
                                <h5>${d.room_name}</h5>
                            </div>
                        </div>
                        <button class="btn" onclick="viewInfo(event)">
                            <div class="d-none" id="taskPersonalID">${d.id}</div>
                            <span class="image-container">
                                <img th:src="@{/img/vista.svg}" src="/img/vista.svg" width="50" height="50" style="margin-right: 5%;" >
                            </span>
                        </button>
                    </div>
            </div>`)
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    return `${day}/${month}/${year} a las ${hours}:${minutes}:${seconds}`;
}

function updateTask(event, idTask) {
    event.preventDefault();

    let params = {
        id: idTask,
        title: $("#selectedTaskTitle").val(),
        user_id: $("#selectedTaskAuthor").val(),
        room_id: $("#selectedTaskRoom").val()
        
    };
    console.log(`PARAMS: id:${params.id}, title:${params.title}, user_id:${params.user_id}, room_id:${params.room_id}`);
    go("/user/updateTask", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTasks").hide()

            // $("#form-newTask")[0].reset()

            //TODO habitación
            $('#divCardsTasks').prepend(
                `<div class="card my-2">
                    <div class="card-content d-flex p-1 bg align-items-center taskCard">
                        <div class="col">
                            <h3>${d.title}</h3>
                            <h5>${d.author}</h5>
                        </div>
                        <div class="col">
                            <div>
                                <h5>${d.creationDate}<h5>
                            </div>
                            <div>
                                <h5>${d.room}</h5>
                            </div>
                        </div>
                        <button class="btn" onclick="viewInfo(event)">
                            <div class="d-none" id="taskPersonalID">${d.id}</div>
                            <img th:src="@{/img/vista.svg}" src="/img/vista.svg" width="50" height="50" style="margin-right: 5%;">
                        </button>
                    </div>
            </div>`)
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}
function viewInfo(event) {
    event.preventDefault();

    const idTask = event.target.parentElement.parentElement.querySelector('#taskPersonalID').textContent;

    go("/user/getTaskInfo/" + idTask, 'GET')
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTaskSelected").hide();

            const formattedDate = formatDateTime(d.creationDate);
            
            $('#divInfoCard form').html(` 
                <div class="mb-2 d-flex">
                    <div class="col-7 titleStyleInfo text-left">·&nbsp; Nombre de la Tarea:</div>
                    <div class="col">
                        <input type="text" id="selectedTaskTitle" class="form-control inputInfo" value="${d.title}" readonly>
                    </div>
                </div>
                <div class="mb-2 d-flex">
                    <div class="col-7 titleStyleInfo text-left">·&nbsp; Autor de la tarea:</div>
                    <div class="col">
                        <input type="text" id="selectedTaskAuthor" class="form-control inputInfo" value="${d.author}" readonly>
                    </div>
                </div>
                <div class="mb-2 d-flex">
                    <div class="col-7 titleStyleInfo text-left">·&nbsp; Fecha de creación:</div>
                    <div class="col">
                        <input type="text" class="form-control inputInfo" value="${formattedDate}" readonly>
                    </div>
                </div>
                <div class="mb-2 d-flex">
                    <div class="col-7 titleStyleInfo text-left">·&nbsp; Habitación:</div>
                    <div class="col">
                        <input type="text" id="selectedTaskRoom" class="form-control inputInfo" value="${d.room}" readonly>
                    </div>
                </div>
                <div class="d-flex justify-content-end">
                    <div class="mx-2">
                        <button type="button" class="btn imgActionsEdit" onclick="updateInfo(event, '${idTask}')" >
                            <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35">
                        </button>
                    </div>
                    <div class="mx-2">
                        <button type="button" class="imgActionsDelete">
                            <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35">
                        </button>
                    </div>
                </div> 
            `);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}


function updateInfo(event, idTask) {
    event.preventDefault();

    go("/user/getTaskInfo/" + idTask, 'GET')
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTaskSelected").hide();

            // Update the form using Thymeleaf context
            $('#divInfoCard form').html(`
            <div class="mb-2 d-flex">
            <div class="col-7 titleStyleInfo text-left">·&nbsp; Nombre de la Tarea:</div>
            <div class="col">
                <input type="text" id="selectedTaskTitle" class="form-control" value="${d.title}" >
            </div>
            </div>
            <div class="mb-2 d-flex">
                <div class="col-7 titleStyleInfo text-left">·&nbsp; Autor de la tarea:</div>
                <div class="col">
                    <input type="text" id="selectedTaskAuthor" class="form-control" value="${d.author}" >
                </div>
            </div>
            <div class="mb-2 d-flex">
                <div class="col-7 titleStyleInfo text-left">·&nbsp; Fecha de creación:</div>
                <div class="col">
                    <input type="text" class="form-control" value="${d.creationDate}" readonly>
                </div>
            </div>
            <div class="mb-2 d-flex">
                <div class="col-7 titleStyleInfo text-left">·&nbsp; Habitación:</div>
                <div class="col">
                    <input type="text" id="selectedTaskRoom" class="form-control" value="${d.room}" >
                </div>
            </div>

            <div class=" text-center d-flex justify-content-between align-items-center">
                <button type="button" class="btn btn-secondary" >Cancelar</button>
                <button type="submit" id="buttonUpdateTask" class="btn btn-primary btnUpdateTask" onclick="updateTask(event, '${idTask}')">Modificar Tarea</button>
            </div>
        `);

            // Remove 'readonly' attribute from editable fields
            $('.form-control').removeAttr('readonly');
            $('.readonly').attr('readonly', true);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}


function filterUpdate(event, x) {

    let params = {
        selectRooms: $("#selectUpdateByRoom").val(),
        selectUsers: $("#selectUpdateByUser").val(),
        selectDates: $("#selectUpdateByDate").val()
    };


    if (params.selectRooms && x === 1) {
        go("/user/filterRoom/" + params.selectRooms, 'GET')
            .then(d => {
                console.log("Success");
                console.log(d);

                $('#divCardsTasks').empty();

                $("#selectUpdateByUser").prop('selectedIndex', 0);
                $("#selectUpdateByDate").prop('selectedIndex', 0);

                if (Object.keys(d).length > 0) {
                    Object.keys(d).forEach(taskKey => {
                        $('#divCardsTasks').append(
                            `<div class="card my-2">
                        <div class="card-content d-flex p-1 bg align-items-center taskCard">
                            <div class="col">
                                <h3>${d[Number(taskKey)].title}</h3>
                                <h5>${d[Number(taskKey)].author}</h5>
                            </div>
                            <div class="col">
                                <div>
                                    <h5>${d[Number(taskKey)].creationDate}<h5>
                                </div>
                                <div>
                                    <h5>${d[Number(taskKey)].room}</h5>
                                </div>
                            </div>
                            <button class="btn" onclick="viewInfo(event)">
                                <div class="d-none" id="taskPersonalID">${d[Number(taskKey)].id}</div>
                                <img th:src="@{/img/vista.svg}" src="/img/vista.svg" alt="Imagen" width="50"
                                    height="50" style="margin-right: 5%;">
                            </button>
                        </div>
                </div>`)
                    });
                } else {
                    $('#divCardsTasks').append(
                        `<div id="noTasks" class="titleStyle">
                    <h4>No se encuentran tareas con ese filtro</h4>
                    <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                    </div>`)
                }
            })
            .catch(e => {
                console.log("Fail Room");
                console.log(e);
            });
    }
    else if (params.selectUsers && x === 2) {
        go("/user/filterUser/" + params.selectUsers, 'GET')
            .then(d => {
                console.log("Success");
                console.log(d);

                $('#divCardsTasks').empty();

                $("#selectUpdateByRoom").prop('selectedIndex', 0);
                $("#selectUpdateByDate").prop('selectedIndex', 0);

                if (Object.keys(d).length > 0) {
                    Object.keys(d).forEach(taskKey => {
                        $('#divCardsTasks').append(
                            `<div class="card my-2">
                        <div class="card-content d-flex p-1 bg align-items-center taskCard">
                            <div class="col">
                                <h3>${d[Number(taskKey)].title}</h3>
                                <h5>${d[Number(taskKey)].author}</h5>
                            </div>
                            <div class="col">
                                <div>
                                    <h5>${d[Number(taskKey)].creationDate}<h5>
                                </div>
                                <div>
                                    <h5>${d[Number(taskKey)].room}</h5>
                                </div>
                            </div>
                            <button class="btn" onclick="viewInfo(event)">
                                <div class="d-none" id="taskPersonalID">${d[Number(taskKey)].id}</div>
                                <img th:src="@{/img/vista.svg}" src="/img/vista.svg" alt="Imagen" width="50"
                                    height="50" style="margin-right: 5%;">
                            </button>
                        </div>
                </div>`)
                    });
                } else {
                    $('#divCardsTasks').append(
                        `<div id="noTasks" class="titleStyle">
                    <h4>No se encuentran tareas con ese filtro</h4>
                    <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                    </div>`)
                }
            })
            .catch(e => {
                console.log("Fail User");
                console.log(e);
            });
    }
    else { // selectDates

        $('#divCardsTasks').empty();

        $("#selectUpdateByRoom").prop('selectedIndex', 0);
        $("#selectUpdateByUser").prop('selectedIndex', 0);

        switch (params.selectDates) {
            case "moreNew":
                //TODO
                break;
            case "moreOld":
                //TODO
                break;
        }

    }

}