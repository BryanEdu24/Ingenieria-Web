Mejoras realizadas por:
- Lucas Bravo Fairen
- David Chaparro García
- Bryan Eduardo Córdova Ascurra
- Clara Sotillo Sotillo

Link del commit de GitHub con las mejoras: https://github.com/BryanEdu24/Ingenieria-Web (En el último commit están las mejoras subidas).

Mejoras que hemos realizado:

PREGUNTAS O CORRECCIONES DEL EXAMEN

- En /user/tasks ahora se pueden ver las notas que hay en cada tarea antes de darle a ver información. Hemos tenido que modificar task.html y el js de task.js.

- Respecto a: "Echo en falta poder marcar mis deudas como saldadas. En este momento, con el mecanismo de reparto de gastos, es muy difícil volver a 0. Un botón de "marcar como pagado" permitiría convertir un número negativo en un hermoso 0 (dejando constancia en el historial, para evitar tramposos)." Para que el balance de un usuario vuelva a 0, debe pagar todas sus deudas desde /user/home con el botón del dolar y/o tener todos los gastos que haya creado pagados por los demás usuarios de la casa.

- Test en Gastos para comprobar que los parentesis de la navbar funcionan. Hemos añadido un nuevo testExamen.feature y modificado ExternalRunner.java para que se ejecute.

- Hacer que las notificaciones de las tareas sean clickeables (en este caso tiene un botón junto a la cruz, que es una flecha (-->)) para ir a ver la infomración de la tarea. Para esta pregunta ha habido que modificar el UserController.java, creando una nueva ruta con un parámetro que es el name de la tarea, un par de javascripts: el encargado de las notificaciones y el de las tareas; por otro lado se ha hecho un pequeño cambio en tasks.html y en styles.css.

- En la navbar:
    · Tareas -> Tareas (n), donde n = cuántas tengo asignadas
    · Gastos -> Gastos (m), donde m = cuánto me deben (si me deben, en verde); o cúanto debo (si debo, en rojo)
Hemos tenido que modificar el fragmento nav.html, el UserController.java para pasarle la infomración necesaria, Task.java añadiendo una nueva query, y task.js y expense.js, en ambos casos para que haya actualizaciones de n y m sin necesidad de hacer F5, por ejemplo cuando te auto creas una tarea, se sumará uno al valor junto a Tareas, sin necesidad de recargar la página.

- Correcion de las terminaciones de las imágenes en ".PNG" a ".png".

OTROS AÑADIDOS PARA LA SUBIDA DE NOTA

- Añadido estado "Incompleta"/"Completada" en ver informacion de tarea. Modificación de tasks.html.

- Update gastos funciona, ahora da fedback cuando no se puede. Modificación de expenses.html y expenses.js.

- Borrar casa en la vista de ADMIN, actualiza la vista mediante AJAX. Para esto hemos tenido que modificar house.js.


