"use strict"

function newTask(event) {
    event.preventDefault();

    let params = {
        title: $("#taskTitle").val(),
        user_id: $("#addTaskSelectUser").val(),
        room_id: $("#addTaskSelectRoom").val(),
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
            $('#divCardsTasks').prepend(`
                <div class="card my-2" id="divCardTasksTask${d.id}">
                    <div class="card-content d-flex p-1 align-items-center taskCard">
                        <div class="col ms-2 my-1">
                            <h3>${d.title}</h3>
                            <h5>${d.userT.username}</h5>
                        </div>

                        <div class="col-4">
                            <div>
                                <h5>${formattedDate}</h5>
                            </div>
                            <div>
                                <h5>${d.room.name}</h5>
                            </div>
                        </div>
                        <button class="btn" onclick="viewInfo(event)">
                            <div class="d-none" id="taskPersonalID">${d.id}</div>
                            <span class="image-container">
                                <img th:src="@{/img/vista.png}" src="/img/vista.png" width="65" height="65" style="margin-right: 5%;" >
                            </span>
                        </button>
                    </div>
                </div>
            `)
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
    return `${day}/${month}/${year} a las ${hours}:${minutes}`;
}

function updateTask(event, idTask) {
    event.preventDefault();

    let params = {
        id: idTask,
        title: $("#selectedTaskTitle").val(),
        user_id: $("#viewTaskSelectUser").val(),
        room_id: $("#viewTaskSelectRoom").val()
    };

    console.log(`PARAMS: id:${params.id}, title:${params.title}, user_id:${params.user_id}, room_id:${params.room_id}`);
    go("/user/updateTask", 'POST', params)
        .then(taskT => {
            console.log("Success");
            console.log(taskT);

            $("#noTasks").hide()

            $("#selectedTaskTitle").attr("readonly", true);
            $("#viewTaskSelectUser").attr("disabled", true);
            $("#viewTaskSelectRoom").attr("disabled", true);

            $("#containerButtonEditDelete").show();
            $("#containerButtonSendCancel").hide();

            $("#selectedTaskTitle").addClass("inputInfo");
            $("#viewTaskSelectUser").addClass("inputInfo");
            $("#viewTaskSelectRoom").addClass("inputInfo");

            $("#divCardTasksTask" + idTask).empty()
            const formattedDate = formatDate(taskT.creationDate);
            $("#divCardTasksTask" + idTask).append(
                `<div class="card-content d-flex p-1 align-items-center taskCard">
                <div class="col ms-2 my-1">
                    <h3>${taskT.title}</h3>
                    <h5>${taskT.userT.username}</h5>
                </div>
                <div class="col-4">
                    <div>
                        <h5>${formattedDate}</h5>
                    </div>
                    <div>
                        <h5>${taskT.room.name}</h5>
                    </div>
                </div>
                <button class="btn" type="button" onclick="viewInfo(event)">
                    <div class="d-none" id="taskPersonalID">${taskT.id}</div>
                    <span class="image-container">
                        <img th:src="@{/img/vista.png}" src="/img/vista.png" width="65" height="65">
                    </span>
                </button>
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
            $("#divDeleteTask").hide();
            $("#taskInfoStateCompleted").hide();
            $("#taskInfoStateNotCompleted").hide();

            // Task Info
            const formattedDate = formatDateTime(d.creationDate);
            $("#divInfoCard").show();
            $("#viewFormTask").show();
            $("#selectedTaskTitle").val(d.title);
            $("#selectedTaskAuthor").val(d.author);
            $("#selectedTaskDate").val(formattedDate);
            $("#viewTaskSelectUser").val(d.userT.id);
            $("#viewTaskSelectRoom").val(d.room.id);
            if (d.done) {
                $("#taskCompletedInput").val(`✅ "Completada"`);
                $("#taskInfoStateCompleted").show();
            }
            else {
                $("#taskNotCompletedInput").val(`❌ "Incompleta"`);
                $("#taskInfoStateNotCompleted").show();
            }
            $("#editTaskButton").attr("onclick", `updateInfo(event, ${idTask})`);
            $("#deleteTaskButton").attr("onclick", `deleteTask(event, ${idTask})`);

            // Task Notes
            $('#inputNotes').show();
            $("#noTaskSelectedNotes").hide();
            $('#carouselNotes').empty();
            d.notes.forEach(i => {
                $('#carouselNotes').prepend(
                    `<div class="card text-center cardNotes my-2">
                        <div class="titleStyleInfo">${i.author}</div>
                        <div><b>${i.message}</b></div>
                    </div>`
                );
            });
            $("#newNoteSendButton").attr("onclick", `newNote(event, ${idTask})`);

            $("#containerButtonEditDelete").show();
            $("#containerButtonSendCancel").hide();

            $("#selectedTaskTitle").addClass("inputInfo");
            $("#viewTaskSelectUser").addClass("inputInfo");
            $("#viewTaskSelectRoom").addClass("inputInfo");
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function deleteTask(event, idTask) {
    event.preventDefault();

    let params = {
        id: idTask,
    };

    go("/user/deleteTask", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#divDeleteTask").show();
            $("#divInfoCard").hide();
            $("#inputNotes").hide()
            $("#noTaskSelectedNotes").show();
            $("#divCardTasksTask" + idTask).hide()
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

            $("#selectedTaskTitle").removeAttr("readonly");
            $("#viewTaskSelectUser").removeAttr("disabled");
            $("#viewTaskSelectRoom").removeAttr("disabled");
            $("#editDeleteButtonTask").hide()
            $("#buttonUpdateTask").attr("onclick", `updateTask(event, ${idTask})`);

            $("#containerButtonEditDelete").hide();
            $("#containerButtonSendCancel").show();
            $("#selectedTaskTitle").removeClass("inputInfo");
            $("#viewTaskSelectUser").removeClass("inputInfo");
            $("#viewTaskSelectRoom").removeClass("inputInfo");
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function cancelUpdateTask(event) {
    event.preventDefault();

    $("#containerButtonSendCancel").hide();            
    $("#containerButtonEditDelete").show();

    $("#selectedTaskTitle").addClass("inputInfo").prop("readonly", true);
    $("#viewTaskSelectUser").addClass("inputInfo").prop("disabled", true);
    $("#viewTaskSelectRoom").addClass("inputInfo").prop("disabled", true);
}

function filterUpdate(event, x) {
    let params = {
        selectRooms: $("#selectUpdateByRoom").val(),
        selectUsers: $("#selectUpdateByUser").val(),
        selectDates: $("#selectUpdateByDate").val(),
        selectHouseId: $("#houseIdForFilters").text()
    };

    console.log("PARAMS " + params);

    if (params.selectRooms && x === 1) {
        go("/user/filterRoom/" + params.selectRooms, 'GET')
            .then(d => {
                console.log("Success");
                console.log(d);

                $("#addTaskButton").hide();
                $("#resetFilterButton").show();

                $('#divCardsTasks').empty();

                $("#selectUpdateByUser").prop('selectedIndex', 0);
                $("#selectUpdateByDate").prop('selectedIndex', 0);

                if (d.length > 0) {
                    tasksCardsAppends(d)
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
                $('#divCardsTasks').empty();
                $('#divCardsTasks').append(
                    `<div id="noTasks" class="titleStyle">
                <h4>No se encuentran tareas con ese filtro</h4>
                <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                </div>`)
            });
    }
    else if (params.selectUsers && x === 2) {
        go("/user/filterUser/" + params.selectUsers, 'GET')
            .then(d => {
                console.log("Success");
                console.log(d);

                $("#addTaskButton").hide();
                $("#resetFilterButton").show();

                $('#divCardsTasks').empty();

                $("#selectUpdateByRoom").prop('selectedIndex', 0);
                $("#selectUpdateByDate").prop('selectedIndex', 0);

                if (d.length > 0) {
                    tasksCardsAppends(d)
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
                $('#divCardsTasks').empty();
                $('#divCardsTasks').append(
                    `<div id="noTasks" class="titleStyle">
                <h4>No se encuentran tareas con ese filtro</h4>
                <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                </div>`)
            });
    }
    else {
        $("#addTaskButton").hide();
        $("#resetFilterButton").show();

        $('#divCardsTasks').empty();

        $("#selectUpdateByRoom").prop('selectedIndex', 0);
        $("#selectUpdateByUser").prop('selectedIndex', 0);

        go("/user/filterTasksHouse/" + params.selectHouseId, 'GET')
            .then(d => {
                if (d.length > 0) {
                    switch (params.selectDates) {
                        case "moreNew":
                            tasksCardsAppends(d.reverse())
                            break;
                        case "moreOld":
                            tasksCardsAppends(d)
                            break;
                    }
                } else {
                    $('#divCardsTasks').append(
                        `<div id="noTasks" class="titleStyle">
                    <h4>No se encuentran tareas con ese filtro</h4>
                    <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                    </div>`)
                }
            })
    }
}

function resetFilters(event, houseId) {
    go("/user/filterTasksHouse/" + houseId, 'GET')
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#addTaskButton").show();
            $("#resetFilterButton").hide();

            $('#divCardsTasks').empty();

            $("#selectUpdateByUser").prop('selectedIndex', 0);
            $("#selectUpdateByDate").prop('selectedIndex', 0);

            if (d.length > 0) {
                tasksCardsAppends(d)
            } else {
                $('#divCardsTasks').append(
                `<div id="noTasks" class="titleStyle">
                    <h4>No se encuentran tareas con ese filtro</h4>
                    <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
                </div>`)
            }
        })

}

function tasksCardsAppends(lista){
    lista.forEach(taskT => {
        const formattedDate = formatDate(taskT.creationDate);
        $('#divCardsTasks').append(
            `<div class="card my-2" id="divCardTasksTask${taskT.id}">
                <div class="card-content d-flex p-1 align-items-center taskCard">
                <div class="col ms-2 my-1">
                    <h3>${taskT.title}</h3>
                    <h5>${taskT.userT.username}</h5>
                </div>
                <div class="col-4">
                    <div>
                        <h5>${formattedDate}</h5>
                    </div>
                    <div>
                        <h5>${taskT.room.name}</h5>
                    </div>
                </div>
                <button class="btn" type="button" onclick="viewInfo(event)">
                    <div class="d-none" id="taskPersonalID">${taskT.id}</div>
                    <span class="image-container">
                        <img th:src="@{/img/vista.png}" src="/img/vista.png" width="65" height="65">
                    </span>
                </button>
            </div>
    </div>`)
    });
}

function newNote(event, idTask) {
    var params = {
        idTask: idTask,
        author: $("#noteAuthor").val(),
        message: $("#noteMessage").val()
    };

    console.log("PARAMS " + params);

    go("/user/newNote", 'POST', params)
        .then(i => {
            console.log("Success");
            console.log(i);
            $('#noteMessage').val(''); // Para borrar nota
            $('#carouselNotes').prepend(
                `<div class="card text-center cardNotes my-2">
                    <div class="titleStyleInfo">${i.author}</div>
                    <div><b>${i.message}</b></div>
                </div>`
            );
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        }
    );
}