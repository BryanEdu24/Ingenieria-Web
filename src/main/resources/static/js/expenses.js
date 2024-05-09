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
    var newQuantity = $("#expenseQuantityEdit").val()

    var params = {
        id: id,
        title: newName,
        quantity: newQuantity
    };

    console.log("PARAMS: ", params);

    go("/user/updateExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $('#idCardExpense' + id).empty()

            $('#idCardExpense' + id).append(
            `<div class="card-content d-flex p-1 align-items-center">
                <div class="col ms-2 my-1">
                    <h3>${d.title}</h3>
                    <h5>${dates.format(d.date, 'dd/MM/yyyy')}</h5>
                </div>
                <div class="col d-flex">
                    <h3>${d.quantity}</h3>
                    <span><h3 style="color: #FF991F;">&ensp;&euro;</h3></span>
                </div>
                <div class="mx-4 text-center">
                    <b><u>Acciones</u></b>
                    <div class="d-flex mt-1">
                        <div>
                            <button type="button" class="imgActionsEdit" data-id="${d.id}" th:onclick="editExpense(${d.id})" title="Editar gasto">
                                <img th:src="@{/img/lapiz.png}" src="/img/lapiz.png" width="35" height="35">
                            </button>
                        </div>
                        <div>
                            <button type="button" class="imgActionsPay" data-id="${d.id}" th:onclick="payExpense(${d.id})" title="Pagar gasto">
                                <img th:src="@{/img/pagar.png}" src="/img/pagar.png" width="35" height="35">
                            </button>
                        </div>
                        <div>
                            <button type="button" class="imgActionsDelete" id="btnDeleteRoom" th:attr="data-name=${d.title}" th:onclick="deleteExpense(${d.id}, \'${d.title}\')" title="Eliminar gasto">
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