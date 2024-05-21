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

    -- HOUSE_1 --

    - Clara -- aa (Rol: User, asignado a casa HOUSE_1).
    - David -- aa (Rol: User, asignado a casa HOUSE_1).
    - Edu -- aa (Rol: User, asignado a casa HOUSE_1).
    - Lucas -- aa (Rol: Manager, asignado a casa HOUSE_1).

    -- HOUSE_2 --

    - Carlos -- aa (Rol: Manager, asignado a casa HOUSE_2).
    - Marta -- aa (Rol: User, asignado a casa HOUSE_2).

    -- Sin casa asignada --

    - Fumu -- aa (Rol: User, sin casa asignada).

Admins (Nombre de usuario, contraseña):
    - Manuel -- aa (Rol: User).

## Entrando como usuario sin casa asignada.
Si entras a la aplicación sin tener una casa asignada se te dará la aportunidad de crear una nueva casa, para lo que se te pedirá un nombre y una contraseña, o la oportunidad de unirte a una casa que ya exista, para lo que se te pedirá el nombre de la casa y su contraseña.

### Las casas que hay creadas en el import.sql por defecto son las siguientes:
Casas (Nombre de la casas, contraseña):
    - HOUSE_1 -- aa.
    - HOUSE_2 -- aa.



### Integrantes del proyecto:
Lucas Bravo Fairen
David Chaparro García
Bryan Eduardo Córdova Ascurra
Fumu Grace Makitu Koudymba
Clara Sotillo Sotillo