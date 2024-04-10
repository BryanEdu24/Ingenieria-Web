
"use strict"

function viewUser(event) {
    event.preventDefault();

    const idTask = event.target.parentElement.parentElement.querySelector('#housePersonalID').textContent;

    go("/user/getHouseInfo/" + idHouse, 'GET')
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noHouseSelected").hide();

            
            
            $('#divInfoCard ').html(` 
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
                        <button type="button" class="btn imgActionsDelete" onclick="deleteTask(event, '${idTask}')" >
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