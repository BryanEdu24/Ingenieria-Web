"use strict"

function newRoom(event){
    event.preventDefault();

    let params = {
        roomName: $("#roomName").val(),
    };
    console.log(`PARAMS: name:${params.roomName}`);
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
                                <img th:src="@{/img/JefeCasa.png}" src="/img/JefeCasa.png" width="50" height="60">
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
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });

}