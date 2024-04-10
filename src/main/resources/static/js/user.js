"use strict"

function deleteUser(id){
    console.log("Botón de bloquear clickeado");
    // var id = $(button).data('id');
    console.log("ID de la tarjeta:", id);
    
    // // Mostrar modal de edición
    $('#deleteMemberModal').modal('show');
    $('#confirmDeleteUserButton').attr("onclick", `confirmDeleteUser(${id})`);

}

function confirmDeleteUser(id){

    var params = {
        id: id,
        
    };

    console.log("PARAMS: ", params);

    go("/user/deleteUser", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}