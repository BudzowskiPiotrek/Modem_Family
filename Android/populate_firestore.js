const admin = require('firebase-admin');

// Descargar el archivo serviceAccountKey.json desde Firebase Console
// Ve a: Configuración del proyecto > Cuentas de servicio > Generar nueva clave privada
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// Manual data - ALL IN ENGLISH
const manualData = {
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

// UI Strings - ALL IN ENGLISH
const uiStrings = {
  app: {
    name: "MODEM FAMILY",
    manual_subtitle: "— USER MANUAL —",
    loading: "Loading content...",
    error_loading: "Error loading data",
    error_not_found: "Data not found"
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
    loading: "Loading...",
    section: "SECTION"
  }
};

// Función para poblar Firestore
async function populateFirestore() {
  try {
    console.log('🔥 Starting Firestore population...\n');

    const manualRef = db.collection('manual');
    const uiStringsRef = db.collection('ui_strings');

    // Populate manual collection
    console.log('📚 Adding manual documents (English)...\n');
    for (const [docId, data] of Object.entries(manualData)) {
      console.log(`📝 Adding document: ${docId}`);
      await manualRef.doc(docId).set(data);
      console.log(`✅ Document ${docId} added successfully\n`);
    }

    // Populate UI strings collection
    console.log('🌐 Adding UI strings (English)...\n');
    for (const [docId, data] of Object.entries(uiStrings)) {
      console.log(`📝 Adding UI strings: ${docId}`);
      await uiStringsRef.doc(docId).set(data);
      console.log(`✅ UI strings ${docId} added successfully\n`);
    }

    console.log('🎉 All documents were added successfully!');
    console.log('\n📊 Summary:');
    console.log(`   - Manual documents: ${Object.keys(manualData).length}`);
    console.log(`   - UI string documents: ${Object.keys(uiStrings).length}`);
    console.log('   - Collections: manual, ui_strings');
    console.log('   - Language: ENGLISH');
    console.log('\n✨ Firestore is ready to use with the Android app!');
    
    process.exit(0);
  } catch (error) {
    console.error('❌ Error populating Firestore:', error);
    process.exit(1);
  }
}

// Ejecutar
populateFirestore();
