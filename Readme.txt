Backend
El backend fue desarrollado en Python utilizando Django REST Framework, con SQL Server como base de datos.
Expone endpoints REST para listar, crear, editar y eliminar tareas, los cuales son consumidos por la aplicación
móvil Android.

-Base de datos
En SQL Server se creó una base de datos llamada ToDoBackend.
Para la conexión entre Django y la base, se configuró un usuario específico con las siguientes credenciales:
user: todouser
pass: ToDoTareas123!

-Usuario administrador (panel Django)
Se creó también un usuario para acceder al panel de administración de Django en
http://127.0.0.1:8000/admin/:
Username: xionico  
Email: xionico@prueba.com  
Password: apptarea

-Instrucciones para ejecutar el backend
En mi caso use un entorno virtual para ejecutar mi backend django en visual studio code. 
Al abrir la carpeta del backend, ejecutar los siguientes comandos para el entorno virtual y levantar el servidor:
- .\venv\Scripts\activate
- cd .\backend_django\
- python manage.py runserver
Con estos pasos el backend se iniciara en: http://127.0.0.1:8000/

Aplicación Android
La aplicación móvil fue desarrollada en Kotlin con arquitectura MVVM, utilizando Room para el almacenamiento local y 
Retrofit para la comunicación con el backend Django.

Al iniciar, la app:
- Obtiene las tareas desde la API o desde la base local si no hay conexión.
- Permite crear, editar, eliminar y marcar tareas como completadas.
-Sincroniza automáticamente los cambios con el backend.
- Ordena las tareas mostrando primero las pendientes y luego las completadas.

