"use strict"

function deletehouseUser(id){
    console.log("Botón de bloquear clickeado");
    // var id = $(button).data('id');
    console.log("ID de la tarjeta:", id);
    
    // // Mostrar modal de edición
    $('#deleteMemberModal').modal('show');
    $('#confirmDeleteUserButton').attr("onclick", `confirmDeleteUser(${id})`);

}

function confirmDeleteUser(id){
    console.log("Llegué a confirm");
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

function viewHouseUserInfo(id){
    
    console.log("Llegué a confirm");
    var params = {
        id: id,
        
    };
    

    console.log("Boton clicado")
    go("/user/getHouseUserInfo/", 'GET', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("HouseUsersCards").removeAttr("hidden");
            
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });


}