# 🚇 CentriMetro - Management System (Modem Family)

This is a comprehensive Java-based desktop application designed for the administrative management of **Metro Málaga** services. The platform integrates user administration, remote file management via FTP, and SMTP communication, all built on a robust architecture synchronized in real-time through Sockets.



## 🚀 Key Features

### 📁 FTP File Management
A fully functional file explorer to interact with remote servers intuitively:
* **Full Operations:** Upload, download, rename, and delete files or directories.
* **Smart Navigation:** Folder-based navigation with double-click support and directory-up functionality.
* **New Folder Creation:** Reliable directory creation with server response validation (handling specific errors like Code 550).
* **Real-time Filtering:** Dynamic search functionality to find files within the current view instantly.

### 🔄 Real-Time Synchronization (Socket System)
A Client-Server architecture implemented to maintain data integrity across multiple users:
* **Event Server:** A centralized `ServerSocket` that manages active client connections.
* **Automatic Broadcast:** Whenever a user performs an action (upload, delete, or create folders), the server instantly notifies all connected clients.
* **Instant Refresh:** Clients receive a `RELOAD` signal and update their UI automatically without user intervention.

### 📧 SMTP Communications & CRUD Administration
* **SMTP Module:** Integrated mailing system for sending reports and notifications.
* **CRUD Module:** Management panel for administrative control over the user database, roles, and system permissions.

---

## 🛠️ Technical Stack

* **Language:** Java
* **GUI:** Java Swing / AWT (Modern, responsive design approach).
* **Networking:** Apache Commons Net (FTP), JavaMail API (SMTP).
* **Concurrency:** Multi-threading for background processes and non-blocking sockets.
* **Architecture:** Model-View-Controller (MVC) Pattern.

---

## 🏗️ Project Structure

The codebase is strictly organized following the **MVC pattern** to ensure scalability and maintainability:

* **`metroMalaga.Model`**: Entity management (`Usuario`) and table data logic (`FTPTableModel`).
* **`metroMalaga.View`**: Decoupled GUI components (`PanelFTP`, `PanelMenu`, `PanelSMTP`).
* **`metroMalaga.Controller`**: Logic orchestrators managing interactions between views and services (`ServiceFTP`, `MenuSelect`).
* **`metroMalaga.Socket`**: Server logic and client threads for real-time event synchronization.

---

## 📦 Installation & Execution

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/BudzowskiPiotrek/Modem_Family.git](https://github.com/BudzowskiPiotrek/Modem_Family.git)
    ```
2.  **Dependencies:** Ensure **Apache Commons Net** and your JDBC connector JARs are added to your Build Path.
3.  **Socket Server:** To enable real-time updates, start the `ServidorSocket.java` class first.
4.  **Application:** Run the main Login class to access the Metro Málaga dashboard.

---

## 👨‍💻 Developed by: Modem Family
Project focused on clean software architecture and efficient data transfer in networked environments.
