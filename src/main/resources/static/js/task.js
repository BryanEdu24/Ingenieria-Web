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
    
    // let params = {
    //     taskID: event.target.parentElement.parentElement.querySelector('#taskPersonalID').textContent,
    // };

    go("/user/getTaskInfo/" + idTask, 'GET')
    .then(d => {
        console.log("Success");
        console.log(d);

        $("#noTaskSelected").hide();

        $("#TitleTaskInfo").text(d.title)
        $("#UserTaskInfo").text(d.user)
        $("#DateTaskInfo").text(d.creationDate)
        $("#AuthorTaskInfo").text(d.author)
        
    })
    .catch(e => {
        console.log("Fail");
        console.log(e);
    });
}