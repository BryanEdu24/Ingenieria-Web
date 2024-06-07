Mejoras realizadas por:

Link del commit de GitHub con las mejoras:

Mejoras que hemos realizado:
    - En /user/tasks ahora se pueden ver las notas que hay en cada tarea antes de darle a ver información. (Pregunta de examen). Hemos tenido que modificar task.html y el js de task.js.

    - Añadido estado "Incompleta"/"Completada" en ver informacion de tarea

    - Update gastos funciona, ahora da fedback cuando no se puede (Hacemos que el catch de la función active un cuadro rojo)

    - Respecto a: "Echo en falta poder marcar mis deudas como saldadas. En este momento, con el mecanismo de reparto de gastos, es muy difícil volver a 0. Un botón de "marcar como pagado" permitiría convertir un número negativo en un hermoso 0 (dejando constancia en el historial, para evitar tramposos)." Para saldar las deudas y que tu saldo vuelva a 0 es necesario que cada uno de los integrantes de la casa paguen el gaston desde user/home en donde aparecerá un simbolo del Euro para pagar. 

    - Borrar casa en la vista de ADMIN, actualiza la vista mediante AJAX

    - En la navbar (Pregunta de examen): 
        · Tareas -> Tareas (n), donde n = cuántas tengo asignadas
        · Gastos -> Gastos (m), donde m = cuánto me deben (si me deben, en verde); o cúanto debo (si debo, en rojo)

    - Test en Gastos para comprobar que los parentesis de la navbar funcionan (Preguta de examen)