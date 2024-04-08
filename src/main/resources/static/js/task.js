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

            $("#noTasks").hide()

            $("#form-newTask")[0].reset()

            //TODO Fecha y habitación
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
                            <img th:src="@{/img/vista.svg}" src="/img/vista.svg" alt="Imagen" width="50" height="50" style="margin-right: 5%;">
                        </button>
                    </div>
            </div>`)
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function updateTask(event, idTask) {
    event.preventDefault();

    let params = {
        id: idTask,
        title: $("#taskTitle").val(),
        user_id: $("#addTaskSelectUser").val(),
        room_id: $("#addTaskSelectRoom").val()
    };
    console.log(`PARAMS: id:${params.id}, title:${params.title}, user_id:${params.user_id}, room_id:${params.room_id}`);
    go("/user/updateTask", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTasks").hide()

            // $("#form-newTask")[0].reset()

            //TODO Fecha y habitación
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
                            <img th:src="@{/img/vista.svg}" src="/img/vista.svg" alt="Imagen" width="50"
                                height="50" style="margin-right: 5%;">
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
  
        // Update the form using Thymeleaf context
        $('#divInfoCard form').html(`
              
            <div class="mb-2 d-flex">
                <div class="col-7 titleStyleInfo text-left">·&nbsp; Nombre de la Tarea:</div>
                <div class="col">
                    <input type="text" class="form-control" value="${d.title}" readonly>
                </div>
            </div>
            <div class="mb-2 d-flex">
                <div class="col-7 titleStyleInfo text-left">·&nbsp; Autor de la tarea:</div>
                <div class="col">
                    <input type="text" class="form-control" value="${d.author}" readonly>
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
                    <input type="text" class="form-control" value="${d.room}" readonly>
                </div>
            </div>
            <hr>
            <div class="d-flex justify-content-end">
                <div class="mx-3">
                    <button type="button" class="btn imgActionsEdit" onclick="updateInfo(event, '${idTask}')" >
                        <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35">
                    </button>
                </div>
                <div class="mx-3">
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
            <div class="row mb-3">
                <label class="col-5 align-self-center"><b>·&nbsp;</b> Nombre de la Tarea:</label>
                <div class="col-7">
                    <input type="text" class="form-control taskTitle" value="${d.title}" >
                </div>
            </div>
            <div class="row mb-3">
                <label class="col-5 align-self-center"><b>·&nbsp;</b> Autor de la tarea:</label>
                <div class="col-7">
                    <input type="text" class="form-control taskAuthor" value="${d.author}" >
                </div>
            </div>
            <div class="row mb-3">
                <label class="col-5 align-self-center"><b>·&nbsp;</b> Fecha de creación:</label>
                <div class="col-7">
                    <input type="text" class="form-control taskCreationDate readonly" value="${d.creationDate}" readonly>
                 </div>
            </div>

            <div class="row mb-3">
                <label class="col-5 align-self-center"><b>·&nbsp;</b> Habitación:</label>
                <div class="col-7">
                    <input type="text" class="form-control taskRoom" value="${d.room}" >
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