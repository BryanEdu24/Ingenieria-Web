"use strict"

function newRoom(event){
    event.preventDefault();

    $("#errorPhotoNoSelected").hide()

    let photo = null

    try{
        photo = document.querySelector('input[name="photoSelect"]:checked').value;
    } catch(e) {
        //Photo dont change
    }

    let params = {
        roomName: $("#roomName").val(),
        roomPhoto: photo
    };


    if(params.roomPhoto && params.roomName){
        console.log(`PARAMS: name:${params.roomName} Photo:${params.roomPhoto}`);
        go("/user/newRoom", 'POST', params)
            .then(d => {
                console.log("Success");
                console.log(d);

                $("#form-newRoom")[0].reset()
                $('#divCardsRoom').prepend(
                    `<div class="card my-2">
                        <div class="card-content d-flex justify-content-between ms-2 p-1">
                            <div class="d-flex">
                                <div class="mx-3">
                                    <img th:src="@{/img/${d.img}.png}" src="/img/${d.img}.png" width="50" height="60">
                                </div>
                                <div class="titleStyle textCardManager">${d.name}</div>
                            </div>
                            <div class="mx-4 text-center">
                                <b><u>Acciones</u></b>
                                <div class="d-flex mt-1">
                                    <div class="mx-3">
                                        <button type="button" class="imgActionsEdit">
                                            <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35" data-bs-toggle="modal" data-bs-target="#editRoomModal">
                                        </button>
                                    </div>
                                    <div class="mx-3">
                                        <button type="button" class="imgActionsDelete">
                                            <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35" data-bs-toggle="modal" data-bs-target="#deleteRoomModal">
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>`
                )

                $("#newRoomModal").modal('hide')
            })
            .catch(e => {
                console.log("Fail");
                console.log(e);
            });
    } else {
        $("#errorPhotoNoSelected").show()
    }

}

function editRoom(id) {
    console.log("Botón de editar clickeado");
    console.log("ID de la tarjeta:", id);
    
    $('#editRoomModal').modal('show'); // Mostrar modal de edición
    $('#buttonRoomUpdate').attr("onclick", `updateRoom(${id})`);
}

function updateRoom(id){
    console.log("Botón de editar habitación clickeado");
    console.log("ID de la tarjeta:", id);
    var newName = $("#roomNameEdit").val();

    var params = {
        id: id,
        name: newName
    };

    console.log("PARAMS: ", params);

    go("/user/updateRoom", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $('#idCardRoom' + id).empty()

            $('#idCardRoom' + id).append(`<div class="card-content d-flex justify-content-between ms-2 p-1">
            <div class="d-flex">
                <div class="mx-3">
                    <img src="/img/${d.img}.png" width="50" height="60">
                </div>
                <div class="titleStyle textCardManager"> ${d.name} </div>
            </div>
            <div class="mx-4 text-center">
                <b><u>Acciones</u></b>
                <div class="d-flex mt-1">
                    <div>
                        <button type="button" class="imgActionsEdit" data-id="${d.id}" onclick="editRoom(${d.id})" title="Editar habitación">
                            <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35" >
                        </button>
                    </div>
                    <div>
                        <button type="button" class="imgActionsDelete" id="btnDeleteRoom" data-name="${d.name}" onclick="deleteRoom(${d.id},\'${d.name}\')" title="Eliminar habitación">
                            <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35" >
                        </button>
                    </div>
                </div>
            </div>`)
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function deleteRoom(id, nameRoom){
    console.log("Botón de borrar clickeado");
    console.log("Datos de deleteRoom:", id, " y ", nameRoom); 
    
    $('#deleteRoomModal').modal('show');
    $('#alertDeleteRoom').hide();
    $('#bodyDeleteModal').empty();
    $('#bodyDeleteModal').prepend(`<h5>¿Estás segur@ que deseas eliminar la habitación: <span style="color: #02B9D8;">${nameRoom}</span>?</h5>`)
    $('#confirmDeleteRoomButton').attr("onclick", `confirmDeleteRoom(${id})`);
}

function confirmDeleteRoom(id){
    var params = {
        id: id,
    };

    console.log("PARAMS: ", params);

    go("/user/deleteRoom", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
            if (d) {
                $('#deleteRoomModal').modal('hide');
                $('#idCardRoom' + id).hide();
            }
            else {                
                $('#alertDeleteRoom').show();
            }
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}