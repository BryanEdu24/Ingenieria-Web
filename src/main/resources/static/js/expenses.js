"use strict"

function newExpense(event) {
    event.preventDefault();

    let params = {
        description: $("#expenseDescription").val(),
        quantity: $("#expenseQuantity").val()
    };
    params.quantity = parseFloat(params.quantity).toFixed(4); // Redondeamos, NO SE TRUNCA

    console.log(`PARAMS: description:${params.description}, quantity:${params.quantity}`);

    go("/user/newExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $("#noExpenses").hide()
            $("#form-newExpense")[0].reset()

            const formattedDate = formatDate(d.date);
            const remainingQuantity = parseFloat(d.remainingQuantity).toFixed(2);

            $('#divCardsExpenses').prepend(`
            <div class="card my-2" id="idCardExpense${d.id}">
                <div class="card-content d-flex p-1 align-items-center">
                    <div class="col ms-2 my-1">
                        <h3>${d.title}</h3>
                        <h5>${formattedDate}</h5>
                    </div>
                    <div class="col">
                        <div class="d-flex">
                            <h5>${d.quantity}</h5>
                            <span class="d-flex">
                                <h5>&euro;, por pagar:&ensp;</h5>
                                <span class="d-flex">
                                    <h5>${remainingQuantity}</h5><h5>&euro;</h5>
                                </span>
                            </span>
                        </div>
                        <h5>${d.author.username}</h5>
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
                                <button type="button" class="imgActionsDelete" id="btnDeleteExpense" attr="data-name=${d.title}" onclick="deleteExpense(${d.id}, \'${d.title}\')" title="Eliminar gasto">
                                    <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35">
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`)
            
            updateBalance()

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

function updateExpense(id) {
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
    params.quantity = parseFloat(params.quantity).toFixed(4); // Redondeamos, NO SE TRUNCA

    go("/user/updateExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);

            $('#idCardExpense' + id).empty()
            const formattedDate = formatDate(d.date);
            const remainingQuantity = parseFloat(d.remainingQuantity).toFixed(2);

            $('#idCardExpense' + id).append(
                `<div class="card-content d-flex p-1 align-items-center">
                    <div class="col ms-2 my-1">
                        <h3>${d.title}</h3>
                        <h5>${formattedDate}</h5>
                    </div>
                    <div class="col">
                        <div class="d-flex">
                            <h5>${d.quantity}</h5>
                            <span class="d-flex">
                                <h5>&euro;, por pagar:&ensp;</h5>
                                <span class="d-flex">
                                    <h5>${remainingQuantity}</h5><h5>&euro;</h5>
                                </span>
                            </span>
                        </div>
                        <h5>${d.author.username}</h5>
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
                                <button type="button" class="imgActionsDelete" id="btnDeleteExpense" attr="data-name=${d.title}" onclick="deleteExpense(${d.id}, \'${d.title}\')" title="Eliminar gasto">
                                    <img th:src="@{/img/basura.png}" src="/img/basura.png" width="35" height="35">
                                </button>
                            </div>
                        </div>
                    </div>
                </div>`);

            updateBalance()

        })
        .catch(e => {
            console.log("Fail");
            console.log("NO PUEDES CAMBIARLA");
        });
}

function deleteExpense(id, nameExpense) {
    console.log("Botón de borrar clickeado");
    console.log("Datos de deleteExpense:", id, " y ", nameExpense);

    $('#deleteModalSpent').modal('show');
    $('#alertDeleteExpense').hide();
    $('#bodyDeleteModal').empty();
    $('#bodyDeleteModal').prepend(`<h5>¿Estás segur@ que deseas eliminar el gasto: <span style="color: #02B9D8;">${nameExpense}</span>?</h5>`)
    $('#confirmDeleteExpenseButton').attr("onclick", `confirmDeleteExpense(${id})`);
}

function confirmDeleteExpense(id) {
    var params = {
        id: id,
    };

    console.log("PARAMS: ", params);

    go("/user/deleteExpense", 'POST', params)
        .then(d => {
            console.log("Success");
            console.log(d);
            if (d) {
                $('#deleteModalSpent').modal('hide');
                $('#idCardExpense' + id).hide();
            }
            else {
                $('#alertDeleteExpense').show();
            }
        })
        .catch(e => {
            console.log("Fail");
            console.log(e);
        });
}

function updateBalance() {

    go("/user/usersByHouse", 'GET')
    .then(d => {
        console.log("Success");
        console.log(d);

        $("#divBalanceCards").empty();

        d.forEach(user => {
            let balanceHtml;
            if (user.balance >= 0) {
                balanceHtml = `
                    <div class="card my-2">
                        <div class="card-content d-flex p-2 justify-content-center align-items-center">
                            <div class="titleStyle textCardManager">${user.username}:&ensp;</div>
                            <h3 class="m-0">
                                <div class="d-flex align-items-center">
                                    <span class="text-success">${user.balance} &euro;</span>
                                    <span class="text-success ms-2">
                                         por recibir
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" class="bi bi-graph-down-arrow" viewBox="0 0 16 16">
                                            <path fill-rule="evenodd" d="M0 0h1v15h15v1H0zm10 11.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-4a.5.5 0 0 0-1 0v2.6l-3.613-4.417a.5.5 0 0 0-.74-.037L7.06 8.233 3.404 3.206a.5.5 0 0 0-.808.588l4 5.5a.5.5 0 0 0 .758.06l2.609-2.61L13.445 11H10.5a.5.5 0 0 0-.5.5"/>
                                        </svg>
                                    </span>
                                </div>
                            </h3>
                        </div>
                    </div>`;
            } else {
                balanceHtml = `
                    <div class="card my-2">
                        <div class="card-content d-flex p-2 justify-content-center align-items-center">
                            <div class="titleStyle textCardManager">${user.username}:&ensp;</div>
                            <h3 class="m-0">
                                <div class="d-flex align-items-center">
                                    <span class="text-danger">${user.balance} &euro;</span>
                                    <span class="text-danger ms-2">
                                         por pagar
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" class="bi bi-graph-up-arrow" viewBox="0 0 16 16">
                                            <path fill-rule="evenodd" d="M0 0h1v15h15v1H0zm10 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 .5.5v4a.5.5 0 0 1-1 0V4.9l-3.613 4.417a.5.5 0 0 1-.74.037L7.06 6.767l-3.656 5.027a.5.5 0 0 1-.808-.588l4-5.5a.5.5 0 0 1 .758-.06l2.609 2.61L13.445 4H10.5a.5.5 0 0 1-.5-.5"/>
                                        </svg>
                                    </span>
                                </div>
                            </h3>
                        </div>
                    </div>`;
            }
            $("#divBalanceCards").append(balanceHtml);
        });
    })
    .catch(e => {
        console.log("Fail");
        console.log(e);
    });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}