"use strict"

if(ws.receive){
    //console.log("WS", ws.receive);
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (destination, obj) => {
        oldFn(destination, obj);

        console.log("OBJ ", obj);
        
        //TODO revisar el Obj
        if (obj.type == "NOTIFICATION") {
            console.log("Received notification");
            let notif = obj.notification;
            let notifsDiv = document.getElementById("divNotificationOffCanvas");
            let p = document.querySelector(`#nav-unread${notif.userId}`);
            
            console.log("New Notification", notif);

            if (p) {
                p.textContent = + p.textContent + 1;
            }

            notifsDiv.insertAdjacentHTML("afterbegin", renderUnreadNotif(notif));
        }
    }
}

function renderUnreadNotif(notif) {
    var aux = "divNotif" + notif.id
    return `<div class="card p-3 mt-2" id="${aux}">
                <div class="card-content d-flex align-items-center">
                    <div class="col d-flex d-flex justify-content-between align-middle textColor">
                        <div class="d-flex align-middle">
                            <h6>${notif.message}</h6>
                        </div>
                        <div class="d-flex align-middle">
                            <div hidden> id </div>
                            <img class="imgActionsDelete" th:src="@{/img/close.svg}" onclick="notificationRead(event, ${notif.id})"
                            src="/img/close.svg" alt="Botón de cerrar" width="20" height="20" title="Cerrar">
                        </div>
                    </div>
                </div>
            </div>`
}

function loadNotificationsOffCanvas(event){
    go("/user/unReadNotifications", 'GET')
            .then(notifications => {
                console.log("Success");
                console.log(notifications);

                $("#divNotificationOffCanvas").empty();

                notifications.forEach(element => {
                   $("#divNotificationOffCanvas").prepend(renderUnreadNotif(element));
                });
            })
            .catch(e => {
                console.log("Fail");
                console.log(e);
            });
}

function notificationRead(event, nId){

    var params = {
        notifId: nId
    }

    go("/user/notificationRead", 'POST', params)
            .then(notifications => {
                console.log("Success");
                console.log(notifications);

                $("#divNotif" + nId).hide()
            })
            .catch(e => {
                console.log("Fail");
                console.log(e);
            });
}