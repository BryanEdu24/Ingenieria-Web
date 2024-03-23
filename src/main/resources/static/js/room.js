"use strict"

function newRoom(event){
    event.preventDefault();

    let params = {
        roomName: $("roomName").val(),
    };
    console.log(`PARAMS: name:${params.roomName}`);
    go("/user/newRoom", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
            
            $("#form-newRoom")[0].reset()
            $('#divCardsRoom').prepend(
                `<div class="card my-2>
                    <div class="card-content d-flex p1 bg alingn-items-center roomCard>
                        <div class="col">
                            <h3>${d.roomName}</h3>
                        </div>
                        <button class="btn">
                            <img th:src="@{/img/vista.png}" src="/img/vista.png" alt="Imagen" width="50"
                                height="50" style="margin-right: 5%;">
                        </button>
                    </div>
                </div>`
            )
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });

}