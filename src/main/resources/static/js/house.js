
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

function deleteHouse(id) { // Banea Casa
    console.log("Botón de bloquear clickeado");
    console.log("ID de la tarjeta:", id);

    // // Mostrar modal de edición
    $('#deleteHouseModal').modal('show');
    $('#titleDeleteModal').empty();
    $('#titleDeleteModal').prepend(`<h5>¿Estás segur@ que deseas eliminar la casa?</h5>`);
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
            $('#houseCard' + id ).hide();
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
                    if(user.roles == "MANAGER") {
                        $("#collapseAreaHouse" + houseId).append(`
                            <div class="card p-1 mb-3 cardsMembersAdmin" id="cardsMembersAdmin${user.id}">
                                <div class="card-content d-flex p-1 align-items-center justify-content-between">
                                    <div class="mx-3 d-flex">
                                        <img th:src="@{/img/JefeCasa.png}" src="/img/JefeCasa.png" width="50" height="60">
                                        <h5 class="ms-2 mb-0 d-flex align-items-center justify-content-center titleStyleInfo">${user.username}</h5>
                                    </div>
                                </div>
                            </div>
                        `);
                    }
                    else {
                        $("#collapseAreaHouse" + houseId).append(`
                        <div class="card p-1 mb-3 cardsMembersAdmin" id="cardsMembersAdmin${user.id}">
                            <div class="card-content d-flex p-1 align-items-center justify-content-between">
                                <div class="mx-3 d-flex">
                                    <img th:src="@{/img/User.png}" src="/img/User.png" width="50" height="60">
                                    <h5 class="ms-2 mb-0 d-flex align-items-center justify-content-center titleStyleInfo">${user.username}</h5>
                                </div>
                                <div class="mx-3">
                                    <button type="button" class="imgActionsDelete" onclick="banHouseUser(event, '${user.id}', \'${user.username}\')">
                                        <img th:src="@{/img/basura.png}" src="/img/basura.png" width="40" height="40">
                                    </button>
                                </div>
                            </div>
                        </div>
                    `);
                    }
                    
                });
            })
            .catch(e => {
                console.log("Fail");
                console.log(e);
            });
    }
}

function banHouseUser(event, id, username){ // Banea User
    console.log("Ban house user");

    $('#BanMemberModal').modal('show');
    $('#bodyBanModal2').empty();
    $('#bodyBanModal2').prepend(`<h5>¿Estás segur@ que deseas eliminar el usuario: <span style="color: #02B9D8;">${username}</span>?</h5>`);
    $('#confirmBanUserButton').attr("onclick", `confirmBanUser(${id})`);
}

function confirmBanUser(id){
    console.log("confirm ban");
    console.log("ID card UserBan:", id);

    var params = {
        id: id,
    };

    console.log("PARAMS: ", params);

    go("/admin/banUser", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            console.log("Success");
            console.log(d);
            if (d) {
                $('#BanMemberModal').modal('hide');
                $('#cardsMembersAdmin' + id).hide();
            }
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}
