Feature: testing app

@task
Scenario: logeo y entrada es task
Given call read('login.feature@login_Clara')
And delay(500)
When click("#buttonExpenses")
And delay(1000)
Then waitForUrl(baseUrl + '/user/expenses')
And delay(1000)
When click('#addExpenseImg')
And delay(1000)
And input('#expenseDescription', 'Gasto de Prueba')
Given input('#expenseQuantity', '20')
And delay(1000)
When click('#buttonNewExpenseCreation')
And delay(1000)
Then match text('#balanceUserNav3') contains '15'