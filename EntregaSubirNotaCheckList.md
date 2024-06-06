Cosas de la correción que son errores.

* LogoTM.png devuelve 404. El nombre del fichero es LogoTM.PNG. Modificad el nombre para que todas las extensiones sean en minúscula (hay varios otros ficheros con este mismo problema, buscad PNG con mayúsculas activas en este enunciado). Este problema sólo es visible en Linux / Mac (donde las mayúsculas son importantes en nombres de fichero).

* Tendría sentido, cuando mostráis "qué ha pasado" en el panel lateral de notificaciones, que se pudiese saltar a la cosa que ha pasado.

* Echo en falta poder marcar mis deudas como saldadas. En este momento, con el mecanismo de reparto de gastos, es muy difícil volver a 0. Un botón de "marcar como pagado" permitiría convertir un número negativo en un hermoso 0 (dejando constancia en el historial, para evitar tramposos).

-----------------------------------------------------------------------------------

Ejercicios del examen, que podemos añadir.

- Haz que notificaciones a una tarea tengan enlaces que, al pulsarlos, tengan el mismo efecto que navegar a la vista de tareas y pulsar en el botón de detalles.

- Haz que en los menús de la barra de navegación aparezcan, en paréntesis, detalles sobre ciertos elementos:
    - Tareas -> Tareas (n), donde n = cuántas tengo asignadas
    - Gastos -> Gastos (m), donde m = cuánto me deben (si me deben, en verde); o cúanto debo (si debo, en rojo)
Sólo cambia el texto de los menús, deben seguir funcionando igual que hasta ahora.

- Escribe una prueba para verificar que, tras añadir un gasto mío, el menú de navegación se actualiza según lo que pone en el ejercicio C. Ten en cuenta que la prueba no pasará si no has implementado el ejercicio C: este ejercicio va sólo de escribir bien la prueba, no de que pase correctamente.

- Explica (pero no implementes) de forma breve qué necesitaríais para que los gastos pudiesen ser sólo de unos pero no necesariamente de otros. Ahora si gasto 100€ y somos 4, cada uno me debe 25€. Pero es concebible (y frecuente) tener gastos que sólo afectan a unos pocos (por ejemplo, si compro carne y tengo un compañero de piso vegetariano, no debería pagar). Para cada fichero que tendrías que tocar o crear, escribe su nombre y (en poco espacio) qué tendrías que cambiar ahí

- Haz que, en la vista de tareas, si la tarea tiene comentarios, se vea el número de comentarios encima del icono de detalles. Así, una tarea con 2 comentarios tendría un 2 encima del icono de detalles; pero otra sin comentarios no tendría ningún número en ese lugar. El botón de detalles debe seguir funcionando, con o sin número.

-----------------------------------------------------------------------------------

Cosas que se nos habían quedado en la entrega 2.

- ADMIN:
    - Banear miembros CHECK (falta comprobar si es manager) 💡
    - Borrar casa (ya no da error, pero no se actualiza la vista) 💡

- MANAGER:
    - modificar contraseña de la casa directamente

- HISTORICO: ✅

- GASTOS:
    - update gastos funciona, pero  no da fedback cuando no se puede (Hacer que el catch de la función active un cuadro rojo
    o así)

- TAREAS:
    - editar completado de una tarea desde vista tarea (puede)
    - Info tarea saber estado

- HOME : ✅

- TEST:
    - arreglar test

- OTROS:
    - Tenemos que hacer que haya más cosas en la BD de cara a la entrega.
    - Actualizar el readme con todos los usuarios que haya y qué se puede hacer.