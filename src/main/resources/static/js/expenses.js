"use strict"

function newExpense(event) {
    event.preventDefault();

    let params = {
        description: $("#expenseDescription").val(),
        quantity: $("#expenseQuantity").val()
    };
    params.quantity = parseFloat(params.quantity).toFixed(2); // Redondeamos, NO SE TRUNCA

    console.log(`PARAMS: description:${params.description}, quantity:${params.quantity}`);

    go("/user/newExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noExpenses").hide()
            $("#form-newExpense")[0].reset()

            const formattedDate = formatDate(d.date);
            $('#divCardsExpenses').prepend(`
            <div class="card my-2" id="idCardExpense${d.id}">
                <div class="card-content d-flex p-1 align-items-center">
                    <div class="col ms-2 my-1">
                        <h3>${d.title}</h3>
                        <h5>${formattedDate}</h5>
                    </div>
                    <div class="col d-flex">
                        <h3>${d.quantity}</h3>
                        <span><h3 style="color: #FF991F;">&ensp;&euro;</h3></span>
                    </div>
                    <div class="mx-4 text-center">
                        <b><u>Acciones</u></b>
                        <div class="d-flex mt-1">
                            <div>
                                <button type="button" class="imgActionsEdit" data-id="${d.id}" onclick="editExpense(${d.id})" title="Editar gasto">
                                    <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35">
                                </button>
                            </div>
                            <div>
                                <button type="button" class="imgActionsPay" data-id="${d.id}" onclick="payExpense(${d.id})" title="Pagar gasto">
                                    <img th:src="@{/img/pagar.png}" src="/img/pagar.png" width="35" height="35">
                                </button>
                            </div>
                            <div>
                                <button type="button" class="imgActionsDelete" id="btnDeleteRoom" attr="data-name=${d.title}" onclick="deleteExpense(${d.id}, \'${d.title}\')" title="Eliminar gasto">
                                    <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35">
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`)

        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function editExpense(id) {
    console.log("Botón de editar gasto clickeado");
    console.log("ID card gasto:", id);
    
    $('#editModalSpent').modal('show'); // Mostrar modal de edición
    $('#buttonExpenseUpdate').attr("onclick", `updateExpense(${id})`);
}

function updateExpense(id){
    console.log("Botón de editar gasto clickeado");
    console.log("ID de la tarjeta:", id);
    var newName = $("#expenseDescriptionEdit").val();
    var newQuantity = $("#expenseQuantityEdit").val();

    var params = {
        id: id,
        title: newName,
        quantity: newQuantity
    };

    console.log("PARAMS: ", params);
    params.quantity = parseFloat(params.quantity).toFixed(2); // Redondeamos, NO SE TRUNCA

    go("/user/updateExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $('#idCardExpense' + id).empty()
            const formattedDate = formatDate(d.date);

            $('#idCardExpense' + id).append(
            `<div class="card-content d-flex p-1 align-items-center">
                <div class="col ms-2 my-1">
                    <h3>${d.title}</h3>
                    <h5>${formattedDate}</h5>
                </div>
                <div class="col d-flex">
                    <h3>${d.quantity}</h3>
                    <span><h3 style="color: #FF991F;">&ensp;&euro;</h3></span>
                </div>
                <div class="mx-4 text-center">
                    <b><u>Acciones</u></b>
                    <div class="d-flex mt-1">
                        <div>
                            <button type="button" class="imgActionsEdit" data-id="${d.id}" onclick="editExpense(${d.id})" title="Editar gasto">
                                <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35">
                            </button>
                        </div>
                        <div>
                            <button type="button" class="imgActionsPay" data-id="${d.id}" onclick="payExpense(${d.id})" title="Pagar gasto">
                                <img th:src="@{/img/pagar.png}" src="/img/pagar.png" width="35" height="35">
                            </button>
                        </div>
                        <div>
                            <button type="button" class="imgActionsDelete" id="btnDeleteRoom" attr="data-name=${d.title}" onclick="deleteExpense(${d.id}, \'${d.title}\')" title="Eliminar gasto">
                                <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35">
                            </button>
                        </div>
                    </div>
                </div>
            </div>`)
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function deleteExpense(id, nameExpense) {
    console.log("Botón de borrar clickeado");
    console.log("Datos de deleteExpense:", id, " y ", nameExpense); 
    
    $('#deleteModalSpent').modal('show');
    //$('#alertDeleteRoom').hide();
    $('#bodyDeleteModal').empty();
    $('#bodyDeleteModal').prepend(`<h5>¿Estás segur@ que deseas eliminar el gasto: <span style="color: #02B9D8;">${nameExpense}</span>?</h5>`)
    $('#confirmDeleteExpenseButton').attr("onclick", `confirmDeleteExpense(${id})`);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}