# TaskMate
Imagina una convivencia sin conflictos, donde cada tarea del hogar se gestiona
sin esfuerzo. Con “TaskMate” los desacuerdos sobre quién hace qué son cosa del
pasado. La aplicación ofrece la posibilidad de añadir las tareas que se necesitan
realizar en cada espacio compartido y asignarlas a uno de los miembros de la
casa, permitiendo también la adición de notas en cada tarea y la repetición de
tareas tanto diaria, semanal o mensualmente. Adicionalmente, resolverá también
otros problemas surgidos de la convivencia como pueden ser la gestión de gastos
del hogar y la organización de notas, como pueden ser la lista de la compra u
otros detalles que necesitan ser recordados. También podrás consultar todos los
cambios que se han realizado tanto en tareas como en gastos en forma de un
histórico. Olvídate de las tensiones y disfruta de una convivencia sin
complicaciones con “TaskMate”.
Roles:
- Jefe de casa (Manager): Persona que crea el grupo de la casa. Puede seleccionar las
habitaciones que se comparten en la casa y añadir a los usuarios al grupo
- Administrador: Gestiona todas las casas, pudiendo banear y eliminar tanto
usuarios como casas enteras.
- Usuarios: Pueden añadir y seleccionar tareas, así como marcarlas como
terminadas.
El jefe de casa puede hacer lo mismo que los usuarios además de sus tareas.

# Funcionamiento de la aplicación
La aplicación está dividida en dos grandes y princiapales funcionalidades, la gestión de tareas y de gastos, que pueden usar los users, ya sean normales o managers. Y otras que han surgido como necesidad de tener dos tipos de usuarios/roles especiales: Admin y Mananger.

Lo primero que pueden hacer los usuarios al entrar en la aplicación es logear o registrarse. Para el registro se pedirá: el nombre, el correo y la contraseña (dos veces); de cara a la aplicación lo que aparecerá a modo de identificación del usuario será el nombre. Por otro lado, en el login se pedirá el nombre y la contraseña.

### Las credenciales que hay en el import.sql por defecto son las siguientes:
Usuarios (Rol User o Manager) (Nombre de usuario, contraseña):

HOUSE_1
- Clara -- aa (Rol: User, asignado a casa HOUSE_1).
- David -- aa (Rol: User, asignado a casa HOUSE_1).
- Edu -- aa (Rol: User, asignado a casa HOUSE_1).
- Lucas -- aa (Rol: Manager, asignado a casa HOUSE_1).

HOUSE_2
- Carlos -- aa (Rol: Manager, asignado a casa HOUSE_2).
- Marta -- aa (Rol: User, asignado a casa HOUSE_2).

Sin casa asignada
- Fumu -- aa (Rol: User, sin casa asignada).

Admins (Nombre de usuario, contraseña):
- Manuel -- aa (Rol: Admin).

## Entrando como usuario sin casa asignada
Si entras a la aplicación sin tener una casa asignada se te dará la aportunidad de crear una nueva casa, para lo que se te pedirá un nombre y una contraseña, o la oportunidad de unirte a una casa que ya exista, para lo que se te pedirá el nombre de la casa y su contraseña.

### Las casas que hay creadas en el import.sql por defecto son las siguientes:
Casas (Nombre de la casas, contraseña):
- HOUSE_1 -- aa.
- HOUSE_2 -- aa.

Una vez te unes a una casa se te envia a la home de la aplicación, pasas a ser un usuario con casa asignada.

## Entrando como usuario con casa asignada
Lo primero que ve el usuario es su home, donde tiene una vista rápida de las tareas y pagos que tiene pendiente. Así como un valor número en el lado del Balance, el cual estará en verde cuando te deban dinero y en rojo cuando debas dinero, es decir, cuando tengas pagos pendientes. El usuario podrá completar/descompletar las tareas y pagar los gastos desde esta vista.

Por otro lado, el usuario tiene la oportunidad de navegar por las diferentes páginas de la web a través de la navbar, así como de deslogear, haciendo click en la foto de arriba a la derecha y posteriormente en cerrar sesión.

### Tareas
En el apartado de tareas los usuarios podrán gestionar todas las tareas de la casa, para ellos tendrán las siguiente funcionalidades:
- Filtros: donde se podrán ver las tareas ordenadas en base a diferentes criterios. (No pudiendo crear nuevas mientras las demás están filtradas).
- Creación de nuevas tareas: dándo click al "+", lo usuarios podrán crear nuevas tareas para la casa. Para esto se le pedirá: el nombre de usuario, a quién se le asgina (pudiendo ser uno mismo) y la habitación a la que pertenece esa tarea.
- Visualización de información de cada tarea, modificación y eliminación: los usuarios pueden ir revisando la información de cada tarea, que es toda la entregada más la fecha de creación; por otro lado se les da la opción de modificar información como el nombre, la asginación y la habitación a la que pertenece; o simplemente la opción de eliminar la tarea.
- Notas: una vez se hace click en el botón que muestra la información de la tarea, también se puede añadir una nota, las cuales quedarán guardadas para que puedan ser leidas por todo aquel que vea la información de la tarea en un futuro.

### Gatos
En el apartado de gastos los usuarios pueden hacer y ver lo siguiente:
- Creación de gatos: dándole click al "+", los usuarios pueden crear un nuevo gasto para la casa, para esto se debe poner el nombre del gasto y la cantidad que lo conforma. Este gasto será repartido a partes iguales entre todos los integrantes de la casa, el que lo crea incluido (su parte se paga automaticamente).
- Visualización de gastos: en esta vista se podrán ver todos los gatos que hay en la casa en ese momento, de ellos se verá el nombre, la fecha en la que se creo, el valor inicial dado, cuánto queda por pagar, quién lo creo y dos acciones, eliminar (solo se podrá hacer si todo el mundo ha pagado) y editar (solo se podrá hacer si nadie lo ha pagado en el momento en que se intenta la eliminación).
- Vista del balance: nos permite ver el estado de las cuentas de todos los usuarios de la casa. Si están en rojo y negativo significa que tienen gastos por pagar. En verde y positivo es que tienen pagos que recibir.

### Manager
Esta vista es accesible unicamente por el manager de la casa, que será uno por casa. Desde esta vista se pueden gestionar los usuarios y habitaciones de la casa.

### Historial
En esta parte de la web se puede ver el historial de diferentes acciones que han tenido lugar dentro de la página, estos movimientos estarás referenciados como TAREA, si esa entrada del historial la ha generado una acción sobre una tarea, o GASTO si por el contrario ha sido por un gasto.

## Entrando como admin
En la vista de admin, se pueden visualizar las diferentes casas que hay en la base de datos. Por otro lado, el admin, puede mostrar más información de cada casa, en este caso, los usuarios que componen la casa. El admin tiene la siguientes acciones:
- Eliminación de casa: si el admin hace click en la papelera que hay en una de las casas, esta quedará borrada y todos sus usuarios pasarán a estar sin casa asignada.
- Eliminación de usuario (baneo): si un usuario hace click en la papelera de uno de los usuarios que hay dentro de una casa, este pasa a estar disabled, es decir, no podrá volver a acceder a la aplicación, queda baneado.

# Integrantes del proyecto:
Lucas Bravo Fairen
David Chaparro García
Bryan Eduardo Córdova Ascurra
Fumu Grace Makitu Koudymba
Clara Sotillo Sotillo