# Documentación Vanner

# 1. Introducción
Este proyecto consiste en el desarrollo de una aplicación móvil basada en el concepto de "Tinder", cuyo objetivo es conectar entrenadores técnicos y preparadores físicos con clientes o empresas que buscan sus servicios. A través de un sistema de emparejamiento ("match"), las partes interesadas pueden interactuar y gestionar colaboraciones laborales de manera fácil y eficiente.

# 2. Objetivos
Facilitar el encuentro entre entrenadores técnicos, preparadores físicos, empresas y clientes interesados en servicios deportivos o de entrenamiento.
Ofrecer un sistema de emparejamiento que permita a los usuarios conectarse e interactuar de manera privada.
Proporcionar una experiencia de usuario intuitiva con opciones de configuración de perfil personalizadas.
Incluir funcionalidades básicas como registro de usuarios, login, recuperación de contraseñas y mensajería interna.

# 3. Alcance
La aplicación está diseñada para tres tipos de usuarios:

- Clientes: Personas que buscan entrenadores o preparadores físicos.
- Entrenadores Técnicos / Preparadores Físicos: Profesionales que buscan ofrecer sus servicios.
- Empresas: Organizaciones que buscan contratar entrenadores para programas o eventos.

# 4. Características Principales
- Registro y Login: Los usuarios pueden crear una cuenta proporcionando la información necesaria y, en caso de olvidar su contraseña, pueden recuperarla mediante un proceso automatizado.
- Sistema de Match: Los usuarios pueden buscar otros perfiles (clientes, empresas o trabajadores) y realizar un "match". Una vez que dos usuarios coinciden, se habilita un chat privado para que puedan interactuar.
- Configuración de Perfil: Cada usuario puede personalizar su perfil, agregar su información personal, experiencia, fotos y cualquier dato relevante para mejorar su visibilidad dentro de la plataforma.
- Mensajería Interna: Una vez realizado un match, los usuarios pueden comunicarse a través de un sistema de mensajería dentro de la aplicación.

# 5. Arquitectura del Sistema
La aplicación se desarrollará siguiendo un modelo cliente-servidor. El backend se encargará de manejar el almacenamiento y la lógica del sistema de emparejamiento, la autenticación y la gestión de usuarios, mientras que el frontend ofrecerá una interfaz de usuario amigable y responsive.

### 5.1. Tecnologías Utilizadas

- Frontend: Android Studio (Java/Kotlin)
- Backend: Java
- Base de Datos: FireBase
- Autenticación: FireBase Authentication
- Servicios de Mensajería: Aun no establecidos 
  
# 6. Casos de Uso
### 6.1. Registro de Usuario
- El usuario ingresa sus datos personales.
- Recibe un correo de verificación.
- Activa su cuenta y accede a la aplicación.

### 6.2. Sistema de Match
- El usuario busca entre los perfiles disponibles.
- Realiza un "swipe" o gesto de aprobación.
- Si ambas partes coinciden, se habilita el chat para comunicarse.

### 6.3. Recuperación de Contraseña
- El usuario selecciona la opción "¿Olvidaste tu contraseña?".
- Recibe un correo con un enlace para restablecer su contraseña.

# 7. Seguridad
La seguridad es una prioridad en esta aplicación. Se implementarán las siguientes medidas:

- Cifrado de Contraseñas: Firebase Authentication se encarga del almacenamiento y gestión segura de las contraseñas, aplicando automáticamente técnicas de hashing para garantizar que las contraseñas no se almacenen en texto plano.
- Autenticación Segura: Firebase Authentication proporciona un sistema robusto de autenticación, protegiendo la identidad de los usuarios y sus datos personales mediante un sistema de tokens seguros.
- Protección de la Información: Las comunicaciones entre usuarios están protegidas utilizando conexiones seguras (SSL/TLS) para evitar la interceptación de datos.

# 8. Pruebas y Validación
Se realizarán pruebas funcionales y de usuario para asegurar que todas las funcionalidades clave de la aplicación (registro, login, match, mensajería, etc.) operen correctamente en diversas plataformas y dispositivos.

# 9. Conclusión
La aplicación busca mejorar la manera en que entrenadores técnicos, preparadores físicos y empresas conectan en el mercado laboral, simplificando el proceso de contratación y creando una plataforma intuitiva y segura para todas las partes involucradas.
