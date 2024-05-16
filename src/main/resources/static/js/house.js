
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

function deleteHouse(id) {
    console.log("Botón de bloquear clickeado");
    console.log("ID de la tarjeta:", id);

    // // Mostrar modal de edición
    $('#deleteHouseModal').modal('show');
    $('#confirmDeleteHouseButton').attr("onclick", `confirmDeleteHouse(${id})`);
}

function confirmDeleteHouse(id) {
    console.log("Llegué a confirm");
    var params = {
        id: id,
    };

    console.log("PARAMS: ", params);

    go("/admin/deleteHouse", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function viewHouseUserInfo(houseId) {

    if ($("#infoUsersHouseButton" + houseId).attr("class") !== "btn collapsed") {
        go("/admin/usersOfHouse/" + houseId, 'GET')
            .then(d => {
                console.log("Success");
                console.log(d);


                $("#collapseAreaHouse" + houseId).empty();

                d.forEach(user => {
                    $("#collapseAreaHouse" + houseId).append(`
                    <div class="card p-1 mb-3" id="cardsMembersAdmin">
                        <div class="card-content d-flex p-1 align-items-center justify-content-between">
                            <div>
                                <h5 class="mb-0 titleStyleInfo">${user.username}</h5>
                            </div>
                            <button type="button" class="imgActionsDelete">
                                <img th:src="@{/img/basura.png}" src="/img/basura.png" width="40" height="40">
                            </button>
                        </div>
                    </div>
                    `);
                });
            })
            .catch(e => {
                console.log("Fail");
                console.log(e);
            });
    }
}