Feature: testing app

@task
Scenario: logeo y entrada es task
Given call read('login.feature@login_Clara')
And delay(500)
When click("#buttonTasks")
And delay(1000)
Then waitForUrl(baseUrl + '/user/task')
And delay(1000)
When click('#addTaskImg')
And delay(1000)
And input('#taskTitle', 'Tarea de Prueba')
Given select('#addTaskSelectUser', '{}c')
Given select('#addTaskSelectRoom', '{}Cocina')
And delay(1000)
When submit().click('#buttonNewTaskCreation')
And delay(1000)