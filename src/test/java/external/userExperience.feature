Feature: testing app

@task
Scenario: logeo y entrada es task
Given call read('login.feature@login_b')
And delay(500)
When click("#buttonTasks")
And delay(1000)
Then waitForUrl(baseUrl + '/user/task')
And delay(1000)
When click( Hay que hacer click en el botón del modal)
And delay(100)
And input('#taskTitle', 'Tarea de Prueba')
And select(Seleccionar option)
And select(Seleccionar option)
When submit().click('#buttonNewTaskCreation')
And delay(100)