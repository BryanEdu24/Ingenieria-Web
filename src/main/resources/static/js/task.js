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
                <div class="card my-2">
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
                                <img th:src="@{/img/vista.svg}" src="/img/vista.svg" width="50" height="50" style="margin-right: 5%;" >
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
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noTasks").hide()

            $("#selectedTaskTitle").attr("readonly", true);
            $("#viewTaskSelectUser").attr("disabled", true);
            $("#viewTaskSelectRoom").attr("disabled", true);

            $("#containerButtonEditDelete").show();
            $("#containerButtonSendCancel").hide();

            //TODO habitación
            // $('#divCardsTasks').prepend(`
            //     <div class="card my-2">
            //         <div class="card-content d-flex p-1 align-items-center taskCard">
            //             <div class="col ms-2 my-1">
            //                 <h3>${d.title}</h3>
            //                 <h5>${d.author}</h5>
            //             </div>
            //             <div class="col-4">
            //                 <div>
            //                     <h5>${formatDate(d.creationDate)}<h5>
            //                 </div>
            //                 <div>
            //                     <h5>${d.room}</h5>
            //                 </div>
            //             </div>
            //             <button class="btn" onclick="viewInfo(event)">
            //                 <div class="d-none" id="taskPersonalID">${d.id}</div>
            //                 <img th:src="@{/img/vista.svg}" src="/img/vista.svg" width="50" height="50" style="margin-right: 5%;">
            //             </button>
            //         </div>
            //     </div>
            // `)
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

            $("#viewFormTask").show();
            $("#selectedTaskTitle").val(d.title);
            $("#selectedTaskAuthor").val(d.author);
            $("#selectedTaskDate").val(formattedDate);
            $("#viewTaskSelectUser").val(d.userT.id);
            $("#viewTaskSelectRoom").val(d.room.id);
            $("#editTaskButton").attr("onclick", `updateInfo(event, ${idTask})`);
            $("#deleteTaskButton").attr("onclick", `deleteTask(event, ${idTask})`);
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

            $("#noTaskSelected").html(`
            <div> TAREA BORRADA </div>
            `)

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

                if (d.length > 0) {
                    d.forEach(taskT => {
                        const formattedDate = formatDate(taskT.creationDate);
                        $('#divCardsTasks').append(
                            `<div class="card my-2">
                        <div class="card-content d-flex p-1 bg align-items-center taskCard">
                            <div class="col">
                                <h3>${taskT.title}</h3>
                                <h5>${taskT.author}</h5>
                            </div>
                            <div class="col">
                                <div>
                                    <h5>${formattedDate}<h5>
                                </div>
                                <div>
                                    <h5>${taskT.room.name}</h5>
                                </div>
                            </div>
                            <button class="btn" onclick="viewInfo(event)">
                                <div class="d-none" id="taskPersonalID">${taskT.id}</div>
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

                if (d.length > 0) {
                    d.forEach(taskT => {
                        const formattedDate = formatDate(taskT.creationDate);
                        $('#divCardsTasks').append(
                            `<div class="card my-2">
                        <div class="card-content d-flex p-1 bg align-items-center taskCard">
                            <div class="col">
                                <h3>${taskT.title}</h3>
                                <h5>${taskT.author}</h5>
                            </div>
                            <div class="col">
                                <div>
                                    <h5>${formattedDate}<h5>
                                </div>
                                <div>
                                    <h5>${taskT.room.name}</h5>
                                </div>
                            </div>
                            <button class="btn" onclick="viewInfo(event)">
                                <div class="d-none" id="taskPersonalID">${taskT.id}</div>
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

        $('#divCardsTasks').append(
            `<div id="noTasks" class="titleStyle">
        <h4>No se encuentran tareas con ese filtro</h4>
        <img th:src="@{/img/noHayTareas.png}" src="/img/noHayTareas.png" height="50" width="50">
        </div>`)

    }

}