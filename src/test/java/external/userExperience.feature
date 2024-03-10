Feature: testing app

@task
Scenario: logeo y entrada es task
Given call read('login.feature@login_b')
And delay(500)
When click("#buttonTasks")
And delay(500)
Then waitForUrl(baseUrl + '/user/task')
And delay(500)