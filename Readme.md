# To-Do List App (Android + Django REST)

Este proyecto demuestra el desarrollo completo de una aplicación **To-Do List** con **persistencia local y remota**, integrando un frontend móvil desarrollado en **Android (Kotlin)** y un backend en **Python (Django REST Framework)** con base de datos **SQL Server**.

---

## Descripción general

La aplicación permite gestionar tareas de forma sencilla, incluso sin conexión a internet.  
Cuando hay conexión, sincroniza automáticamente los datos con el backend.

Este proyecto fue diseñado para mostrar habilidades de desarrollo **full stack**, manejo de **APIs REST**, y buenas prácticas en la arquitectura móvil (**MVVM**) y backend (**Django REST Framework**).

---

## Backend – Django REST Framework

El backend fue desarrollado en **Python 3.12** con **Django REST Framework**, utilizando **SQL Server** como base de datos principal.  
Expone endpoints REST para crear, listar, editar y eliminar tareas, los cuales son consumidos por la aplicación móvil.

### Base de datos
- **Nombre:** `ToDoBackend`  
- **Motor:** SQL Server  
- **Usuario:** `todouser`  
- **Contraseña:** `ToDoTareas123!`

La conexión se realiza mediante el archivo `settings.py`, configurado para este usuario.

### Usuario administrador (panel Django)
Se creó un usuario para acceder al panel de administración disponible en  
http://127.0.0.1:8000/admin/:

Username: xionico  
Email: xionico@prueba.com  
Password: apptarea

### Instrucciones para ejecutar el backend
En mi caso use un entorno virtual para ejecutar mi backend django en visual studio code. 

### Crear entorno virtual
python -m venv venv

Al abrir la carpeta del backend, ejecutar los siguientes comandos para el entorno virtual y levantar el servidor:
- .\venv\Scripts\activate
- cd .\backend_django\
- python manage.py runserver

Con estos pasos el backend se iniciara en: http://127.0.0.1:8000/

## Aplicación Android

La aplicación móvil fue desarrollada en Kotlin, siguiendo el patrón MVVM.
Implementa Room para la persistencia local y Retrofit para la comunicación con la API Django.


Funcionalidades principales:
- Obtiene las tareas desde la API o desde la base local si no hay conexión.
- Permite crear, editar, eliminar y marcar tareas como completadas.
- Sincroniza automáticamente los cambios con el backend cuando vuelve la conexión.
- Ordena las tareas mostrando primero las pendientes y luego las completadas.


## Tecnologías utilizadas:

Backend:
Python 3.12
Django 5.x
Django REST Framework
SQL Server

Frontend (Android):
Kotlin
Android Studio
Room
Retrofit
LiveData / ViewModel (MVVM)

To-Do-List-Android-Kotlin/

│

├── android-app/           # Proyecto Android Studio

│           ├── app/

│           └── build.gradle

│

└── ToDoBackend/

        ├── backend_django/    # Proyecto Django REST
        
        └──venv/              # Entorno virtual (no subido)
    


## Capturas de la aplicación

### Pantalla principal
![Pantalla principal](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/captura%20inicio.jpg)

### Vista creación de nueva tarea
![Creación Nueva Tarea](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/Captura%20nueva%20tarea.jpg)

### Vista de nueva tarea
![Visualizacion Nueva Tarea](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/Captura%20visualizacion%20de%20nueva%20tarea.jpg)

### Vista de edicion o eliminacion de una tarea
![Edición o Eliminación de Tarea](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/Captura%20vista%20edicion%20o%20eliminacion.jpg)

### Vista de API Rest
![Visualización API Rest](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/Captura%20visualizacion%20API%20Rest.jpg)

### Vista SQL Server
![Visualizacion SQL Server](https://github.com/tatianadelsoglio/To-Do-List-Android-Kotlin/blob/main/Captura%20visualizacion%20SQL%20Server.jpg)



Nota final: Este proyecto tiene fines demostrativos y educativos. Su objetivo es exhibir la integración entre una app móvil Android y un backend Django, con buenas prácticas de desarrollo, estructuración y documentación.

-------------------------------------------------------------------
Autora:
Tatiana Delsoglio
Desarrolladora de Software
📍 Córdoba, Argentina

