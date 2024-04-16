"use strict"

if(ws.receive){
    console.log("WS", ws.receive);
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (destination, obj) => {
        oldFn(destination, obj);

        if (obj.type == "NOTIFICATION") {
            console.log("Received notification");
            let p = document.querySelector("#nav-unread");
            if (p) {
                p.textContent = + p.textContent + 1;
            }

            let notif = obj.notification;
            let notifsDiv = document.getElementById("div-card-list");

            notifsDiv.insertAdjacentHTML("afterbegin", renderUnreadNotif(notif));
            
        }
    }
}

function renderUnreadNotif(notif) {
    return `<div class="card p-3 mt-2">
                <div class="card-content d-flex align-items-center">
                    <div class="col d-flex d-flex justify-content-between align-middle textColor">
                        <div class="d-flex align-middle">
                            <h6>${notif.message}</h6>
                        </div>
                        <div class="d-flex align-middle">
                            <div hidden> id </div>
                            <img class="imgActionsDelete" th:src="@{/img/close.svg}"
                            src="/img/close.svg" alt="Botón de cerrar" width="20" height="20" title="Cerrar">
                        </div>
                    </div>
                </div>
            </div>`
}