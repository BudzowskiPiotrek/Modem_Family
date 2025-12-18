const admin = require('firebase-admin');

const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// ============================================
// ENGLISH CONTENT
// ============================================

const manualDataEN = {
  introduccion: {
    title: "Introduction",
    paragraph1: "Welcome to the user manual for the centiMetro de Málaga management system. This document has been prepared by the Modem Family development team with the goal of guiding users in using the new information management software.",
    paragraph2: "This application solves these problems by unifying three tools into a single screen:",
    features: [
      "Content Manager (CRUD): A centralized system to manage all information about lines, stations, trips, trains, and depots in an organized manner.",
      "FTP System: A secure space to upload and download documents, eliminating the need to use pendrives or insecure public clouds.",
      "Email Management: Our own email client to communicate between departments without depending on external services."
    ],
    paragraph3: "Throughout this manual we will explain, step by step, how to use each of these functions in a simple way, whether you are a user querying data or a system administrator."
  },

  login: {
    title: "Login",
    description: "In Login we have two fields where the user must enter the credentials provided by the administrator, and gain access to the application.",
    field_description: "In the first field, the user must enter the username that is registered by the administrator and in the second field the password.",
    button_action: "Once you have entered the different data in the fields, when you press the red button that says \"LOGIN\" just below, the system will verify that the credentials are correct.",
    success_message: "If the credentials are correct, you will have direct access to the application, landing on the main menu where you can choose the module you want to access. But, if the credentials are incorrect, you will not be allowed to enter the application, therefore the system will ask you to enter new credentials in the fields.",
    session_info: "The user's session remains open until the user closes the program."
  },

  menu: {
    title: "Menu",
    description: "The visualization of the different systems will be done through tabs"
  },

  gestion_correos: {
    title: "Email Management",
    intro: "By selecting the SMTP tab, a tab will open with email management.",
    intro_list_title: "You will find:",
    sections: [
      "Section to send emails.",
      "Section to view your email inbox."
    ],
    envio_title: "Email Sending Section",
    envio_intro: "You will find:",
    envio_fields: [
      "Text field to enter the email address to which you are going to send the email.",
      "Text field to enter the email subject.",
      "Text field to enter the email body.",
      "Three buttons:"
    ],
    envio_button_attach: "\"Attach File\" button will open the file explorer to attach all files to send in the email.",
    envio_button_clear: "\"Clear\" button removes all attached files. Next to it there is a text showing the names of the attached files and if the mouse is over that text all the files will appear.",
    envio_button_send: "\"Send Email\" button sends the email with all the filled data.",
    recepcion_title: "Email Reception Section",
    recepcion_intro: "You will find:",
    recepcion_table: "On the left an empty table that is automatically filled with emails from the inbox (when you press the \"Refresh\" button it updates a little faster) and every time an email arrives it refreshes on its own.",
    recepcion_detail: "On the right the information inside the email will appear depending on the selected email.",
    recepcion_buttons_intro: "Below there are four buttons:",
    recepcion_buttons: [
      "\"Refresh\" updates the inbox and displays the emails currently in the inbox.",
      "\"Mark Read/Unread\" changes the status of the email between read and unread.",
      "\"Download .eml\" downloads the selected email in .eml file format.",
      "\"Delete Email\" deletes the selected email."
    ]
  },

  crud: {
    title: "CRUD",
    intro: "By selecting the CRUD tab, a tab will open with the different tables inside the database. In the tables will be the information of each one with their records.",
    permissions: "In each table, there are different functions such as inserting records, modifying records or deleting records but to use these functions the user must have the \"Administrator\" role since users with a \"User\" role will not have permissions to perform these actions, they will only have permission to read the records but without being able to interact with them.",
    navigation: "To navigate through the different tables, there is a panel on the left side where the names of the tables appear and when you click on a name, the information of the table appears on the right panel, divided into grids depending on the fields it has.",
    actions: "In the last field inside the tables there is an \"Actions\" section where you will find the buttons to perform editing actions through the \"Edit\" button or the delete action through the \"Delete\" button.",
    insert_edit: "In the lower panel, to add records when the user is \"Administrator\", the names of the fields we find in the table appear but now with a text box to manually insert the necessary data. But if what the user wants is to edit a record, they must select a row/record from the table so that the data inside that row/record is written in the text box and to edit the records it is as simple as changing the fields you want by writing over it.",
    edit_mode: "When clicking edit on a record, the lower panel to add new records changes to an edit panel for the selected record. The data from that record appears in the editable fields, and once we have everything updated we can click \"Update\" to confirm the changes. In case of error or wanting to keep the previous data, you can click \"Cancel\" to exit the edit mode."
  },

  ftp: {
    title: "FTP",
    intro: "By selecting the FTP tab, a tab will open",
    interface_title: "1. Main Interface",
    interface_description: "When opening the manager, you will see a table with the server content. The information is organized in four columns:",
    interface_columns: [
      "NAME: File or folder name.",
      "SIZE: Size occupied on the server.",
      "DATE: Last modification date.",
      "BUTTONS: Quick actions for each element."
    ],
    actions_title: "2. Actions on Files and Folders",
    actions_intro: "In the right column (Buttons), you have three specific tools for each row:",
    actions_list: [
      "Download (Blue): Click on the down arrow icon to download the file to your local computer.",
      "Delete (Red): Click on the \"X\" to permanently delete the file or directory from the server.",
      "Rename (Yellow): Click on the pencil icon to change the name of the selected element."
    ],
    navigation_title: "3. Navigation and Directory Management",
    navigation_intro: "At the bottom left, you will find the main toolbar:",
    navigation_buttons: [
      "Upload File (Red Button ⤒): Opens a local file explorer to upload documents to the server.",
      "Go Up Level (Gray Button 🗹): Allows you to go back to the previous (parent) folder.",
      "Create Folder (Green Button 📁 New Folder): Requests a name and creates a new directory in the current location.",
      "Return (Red Button Return): Closes the FTP session and returns to the application's main menu."
    ],
    search_title: "4. Search and Filtering",
    search_description: "To quickly locate a file among a long list, use the text box in the lower right corner:",
    search_steps: [
      "Type the file name in the Filter field.",
      "The table will automatically update showing only matches in real time."
    ],
    doubleclick_title: "5. Navigation via Double Click",
    doubleclick_description: "In addition to buttons, you can interact directly with the table:",
    doubleclick_action: "Double-clicking on a folder will automatically open its contents.",
    notes_title: "Additional Notes",
    notes: [
      "Automatic Update: The system has a refresh thread (or socket system) that keeps the file list updated if other users make changes.",
      "Safe Close: When closing the window or pressing \"Return\", the application safely disconnects notifications and the session with the server."
    ]
  }
};

const uiStringsEN = {
  app: {
    name: "MODEM FAMILY",
    manual_subtitle: "— USER MANUAL —",
    loading: "Loading content...",
    error_loading: "Error loading data",
    error_not_found: "Data not found",
    language_selector: "🌐 Language"
  },
  
  menu: {
    title: "MODEM FAMILY",
    subtitle: "— USER MANUAL —",
    select_section: "Select a section",
    btn_introduction: "◆ INTRODUCTION",
    btn_login: "◆ LOGIN",
    btn_menu: "◆ MENU",
    btn_emails: "◆ EMAIL MANAGEMENT",
    btn_crud: "◆ CRUD",
    btn_ftp: "◆ FTP"
  },
  
  common: {
    btn_back: "← BACK",
    btn_return: "← RETURN TO MENU",
    btn_language: "🌐",
    loading: "Loading...",
    section: "SECTION"
  },
  
  language_dialog: {
    title: "Select Language",
    english: "English",
    spanish: "Español"
  }
};

// ============================================
// SPANISH CONTENT  
// ============================================

const manualDataES = {
  introduccion: {
    title: "Introducción",
    paragraph1: "Bienvenido al manual de usuario del sistema de gestión para centiMetro de Málaga. Este documento ha sido elaborado por el equipo de desarrollo de Modem Family con el objetivo de guiar a los usuarios en el uso del nuevo software de gestión de información.",
    paragraph2: "Esta aplicación soluciona esos problemas unificando tres herramientas en una sola pantalla:",
    features: [
      "Gestor de contenido (CRUD): Un sistema centralizado para administrar toda la información de líneas, estaciones, viajes, trenes y cocheras de forma ordenada.",
      "Sistema FTP: Un espacio seguro para subir y descargar documentos, eliminando la necesidad de usar pendrives o nubes públicas inseguras.",
      "Gestión de correos: Un cliente de email propio para comunicarnos entre departamentos sin depender de servicios externos."
    ],
    paragraph3: "A lo largo de este manual te explicaremos, paso a paso, cómo utilizar cada una de estas funciones de manera sencilla, tanto si eres un usuario que consulta datos como si eres administrador del sistema."
  },

  login: {
    title: "Login",
    description: "En el Login tenemos dos campos donde el usuario tendrá que ingresar las credenciales que le proporcione el administrador, y conseguir acceso a la aplicación.",
    field_description: "En el primer campo, el usuario deberá ingresar el nombre de usuario que está registrado por el administrador y en el segundo campo la contraseña.",
    button_action: "Una vez haya ingresado los distintos datos en los campos, al darle al botón rojo que pone \"LOGIN\" justo debajo el sistema verificará que las credenciales son correctas.",
    success_message: "Si las credenciales son correctas tendrá acceso directo a la aplicación, aterrizando en el menú principal donde podrá elegir el módulo al que quiere acceder. Pero, si las credenciales son incorrectas no le dejará entrar a la aplicación, por lo tanto el sistema pedirá que ingrese unas nuevas credenciales en los campos.",
    session_info: "La sesión del usuario se queda abierta hasta que el usuario cierre el programa."
  },

  menu: {
    title: "Menú",
    description: "La visualización de los distintos sistemas se hará mediante pestañas"
  },

  gestion_correos: {
    title: "Gestión de correos",
    intro: "Al seleccionar la pestaña de SMTP se abrirá una pestaña con la gestión de correos electrónicos.",
    intro_list_title: "Encontraremos:",
    sections: [
      "Sección para enviar correos.",
      "Sección para visualizar la bandeja de entrada de tu correo."
    ],
    envio_title: "Sección de envío de correos electrónicos",
    envio_intro: "Encontrarás:",
    envio_fields: [
      "Campo de texto para introducir la dirección de correo electrónico a la que va a enviar el correo.",
      "Campo de texto para introducir el asunto del correo.",
      "Campo de texto para introducir el cuerpo del correo.",
      "Tres botones:"
    ],
    envio_button_attach: "Botón \"Attach File\" abrirá el explorador de archivos para adjuntar todos los archivos para enviar en el correo.",
    envio_button_clear: "Botón \"Clear\" elimina todos los archivos adjuntados. Junto a él hay un texto en el que aparecen los nombres de los archivos adjuntos y si el ratón se encuentra encima de ese texto aparecerán todos los archivos.",
    envio_button_send: "Botón \"Send Email\" envía el correo electrónico con todos los datos rellenos.",
    recepcion_title: "Sección de recepción de correos electrónicos",
    recepcion_intro: "Encontrarás:",
    recepcion_table: "A la izquierda una tabla vacía que se rellena automáticamente con los correos electrónicos de la bandeja de entrada (cuando pulsas el botón \"Refresh\" se actualiza un poco más rápido) y cada vez que llega un correo se refresca solo.",
    recepcion_detail: "A la derecha aparecerá la información dentro del correo electrónico dependiendo del correo que esté seleccionado.",
    recepcion_buttons_intro: "Debajo hay cuatro botones:",
    recepcion_buttons: [
      "\"Refresh\" actualiza la bandeja de entrada y visualiza los correos que hay actualmente en la bandeja de entrada.",
      "\"Mark Read/Unread\" cambia el estado del correo electrónico entre leído y no leído.",
      "\"Download .eml\" descarga el correo electrónico seleccionado en formato de archivo .eml.",
      "\"Delete Email\" elimina el correo electrónico seleccionado."
    ]
  },

  crud: {
    title: "CRUD",
    intro: "Al seleccionar la pestaña de CRUD se abrirá una pestaña con las distintas tablas que hay dentro de la base de datos. En las tablas estará la información de cada una con los registros de estas.",
    permissions: "En cada tabla, hay distintas funciones como insertar registros, modificar registros o eliminar registros pero para usar estas funciones el usuario debe tener el rol de \"Administrador\" ya que los usuarios con un rol de \"Usuario\" no tendrán permisos para realizar estas acciones, solo tendrán permiso para leer los registros pero sin poder interactuar con ellos.",
    navigation: "Para navegar por las distintas tablas, hay un panel en la parte izquierda donde aparecen los nombres de las tablas que al pulsar sobre algún nombre aparece en el panel de la derecha la información de la tabla, dividida por cuadrículas dependiendo de los campos que tenga esta.",
    actions: "En el último campo dentro de las tablas hay una sección de \"Acciones\" donde se encontrarán los botones para realizar las acciones de edición a través del botón \"Editar\" o la acción de eliminar a través del botón \"Eliminar\".",
    insert_edit: "En el panel inferior, para añadir registros cuando el usuario es \"Administrador\", aparecen los nombres de los campos que encontramos en la tabla pero ahora con una caja de texto para insertar a mano los datos necesarios. Pero si lo que quiere el usuario es editar un registro, deberá seleccionar una fila/registro de la tabla para que los datos que hay dentro de esa fila/registro se escriben en la caja de texto y para editar los registros es tan simple como cambiar los campos que quieras escribiendo sobre este.",
    edit_mode: "Al pulsar en editar un registro, el panel inferior para añadir nuevos registros cambia a un panel de edición del registro seleccionado. Los datos de dicho registro aparecen en los campos editables, y una vez tengamos todo actualizado podemos pulsar \"Actualizar\" para confirmar los cambios. En caso de error o de querer mantener los datos previos se puede pulsar \"Cancelar\" para salir de la edición."
  },

  ftp: {
    title: "FTP",
    intro: "Al seleccionar la pestaña de FTP se abrirá una pestaña",
    interface_title: "1. Interfaz Principal",
    interface_description: "Al abrir el gestor, verás una tabla con el contenido del servidor. La información se organiza en cuatro columnas:",
    interface_columns: [
      "NAME: Nombre del archivo o carpeta.",
      "SIZE: Tamaño ocupado en el servidor.",
      "DATE: Última fecha de modificación.",
      "BUTTONS: Acciones rápidas por cada elemento."
    ],
    actions_title: "2. Acciones sobre Archivos y Carpetas",
    actions_intro: "En la columna de la derecha (Buttons), dispones de tres herramientas específicas para cada fila:",
    actions_list: [
      "Descargar (Azul): Haz clic en el icono de la flecha hacia abajo para bajar el archivo a tu equipo local.",
      "Eliminar (Rojo): Haz clic en la \"X\" para borrar permanentemente el archivo o directorio del servidor.",
      "Renombrar (Amarillo): Haz clic en el icono del lápiz para cambiar el nombre del elemento seleccionado."
    ],
    navigation_title: "3. Navegación y Gestión de Directorios",
    navigation_intro: "En la parte inferior izquierda, encontrarás la barra de herramientas principal:",
    navigation_buttons: [
      "Subir Archivo (Botón Rojo ⤒): Abre un explorador de archivos local para cargar documentos al servidor.",
      "Subir Nivel (Botón Gris 🗹): Te permite retroceder a la carpeta anterior (padre).",
      "Crear Carpeta (Botón Verde 📁 New Folder): Solicita un nombre y crea un nuevo directorio en la ubicación actual.",
      "Volver (Botón Rojo Return): Cierra la sesión FTP y regresa al menú principal de la aplicación."
    ],
    search_title: "4. Búsqueda y Filtrado",
    search_description: "Para localizar rápidamente un archivo entre una lista larga, utiliza el cuadro de texto en la esquina inferior derecha:",
    search_steps: [
      "Escribe el nombre del archivo en el campo Filtrar.",
      "La tabla se actualizará automáticamente mostrando solo las coincidencias en tiempo real."
    ],
    doubleclick_title: "5. Navegación mediante Doble Clic",
    doubleclick_description: "Además de los botones, puedes interactuar directamente con la tabla:",
    doubleclick_action: "Hacer doble clic sobre una carpeta abrirá su contenido automáticamente.",
    notes_title: "Notas Adicionales",
    notes: [
      "Actualización Automática: El sistema cuenta con un hilo de refresco (o sistema de sockets) que mantiene la lista de archivos actualizada si otros usuarios realizan cambios.",
      "Cierre Seguro: Al cerrar la ventana o pulsar \"Return\", la aplicación desconecta de forma segura las notificaciones y la sesión con el servidor."
    ]
  }
};

const uiStringsES = {
  app: {
    name: "MODEM FAMILY",
    manual_subtitle: "— MANUAL DE USUARIO —",
    loading: "Cargando contenido...",
    error_loading: "Error al cargar datos",
    error_not_found: "Datos no encontrados",
    language_selector: "🌐 Idioma"
  },
  
  menu: {
    title: "MODEM FAMILY",
    subtitle: "— MANUAL DE USUARIO —",
    select_section: "Selecciona una sección",
    btn_introduction: "◆ INTRODUCCIÓN",
    btn_login: "◆ LOGIN",
    btn_menu: "◆ MENÚ",
    btn_emails: "◆ GESTIÓN DE CORREOS",
    btn_crud: "◆ CRUD",
    btn_ftp: "◆ FTP"
  },
  
  common: {
    btn_back: "← VOLVER",
    btn_return: "← VOLVER AL MENÚ",
    btn_language: "🌐",
    loading: "Cargando...",
    section: "SECCIÓN"
  },
  
  language_dialog: {
    title: "Seleccionar Idioma",
    english: "English",
    spanish: "Español"
  }
};

// ============================================
// POPULATE FUNCTION
// ============================================

async function populateFirestore() {
  try {
    console.log('🔥 Starting Firestore population (Multi-language)...\n');

    // Populate ENGLISH collections
    console.log('🇬🇧 Adding ENGLISH content...\n');
    const manualENRef = db.collection('manual_en');
    for (const [docId, data] of Object.entries(manualDataEN)) {
      console.log(`📝 Adding manual_en/${docId}`);
      await manualENRef.doc(docId).set(data);
    }

    const uiStringsENRef = db.collection('ui_strings_en');
    for (const [docId, data] of Object.entries(uiStringsEN)) {
      console.log(`📝 Adding ui_strings_en/${docId}`);
      await uiStringsENRef.doc(docId).set(data);
    }

    // Populate SPANISH collections
    console.log('\n🇪🇸 Adding SPANISH content...\n');
    const manualESRef = db.collection('manual_es');
    for (const [docId, data] of Object.entries(manualDataES)) {
      console.log(`📝 Adding manual_es/${docId}`);
      await manualESRef.doc(docId).set(data);
    }

    const uiStringsESRef = db.collection('ui_strings_es');
    for (const [docId, data] of Object.entries(uiStringsES)) {
      console.log(`📝 Adding ui_strings_es/${docId}`);
      await uiStringsESRef.doc(docId).set(data);
    }

    console.log('\n🎉 All documents were added successfully!');
    console.log('\n📊 Summary:');
    console.log(`   - English manual documents: ${Object.keys(manualDataEN).length}`);
    console.log(`   - English UI strings: ${Object.keys(uiStringsEN).length}`);
    console.log(`   - Spanish manual documents: ${Object.keys(manualDataES).length}`);
    console.log(`   - Spanish UI strings: ${Object.keys(uiStringsES).length}`);
    console.log('   - Collections: manual_en, manual_es, ui_strings_en, ui_strings_es');
    console.log('\n✨ Firestore is ready for multi-language support!');
    
    process.exit(0);
  } catch (error) {
    console.error('❌ Error populating Firestore:', error);
    process.exit(1);
  }
}

// Ejecutar
populateFirestore();
