"use strict"

function deletehouseUser(id, nameUser, rolUser){
    console.log("Botón de borrar user clickeado");
    
    console.log("Datos de deleteUser:", id, " y ", nameUser, " es: ", rolUser); 
    
    $('#deleteMemberModal').modal('show');
    $('#alertDeleteUser').hide();
    $('#bodyDeleteModal2').empty();
    $('#bodyDeleteModal2').prepend(`<h5>¿Estás segur@ que desea eliminar <span style="color: #FFB919;">${nameUser}</span>?</h5>`)
    if (rolUser == "MANAGER") {
        $('#divDeleteManager').show();
    }
    else {
        $('#divDeleteManager').hide();
    }
    $('#confirmDeleteUserButton').attr("onclick", `confirmDeleteUser(${id})`);
}

function confirmDeleteUser(id){
    console.log("Llegué a confirmDeleteUser");
    var managerId = $('#selectNewManager').val();
    var params = {
        id: id,
        newManager: managerId? managerId:-1
    };

    console.log("PARAMS: ", params);

    go("/user/deleteUser", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
            if (d) {
                $('#deleteMemberModal').modal('hide');
                $('#idCardUser' + id).hide();
            }
            else {                
                $('#alertDeleteUser').show();
            }
        })
        .catch(e => {    
            console.log("Fail :/");        
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