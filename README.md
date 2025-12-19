# 🚇 CentriMetro - Management System (Modem Family)

This is a comprehensive Java-based desktop application designed for the administrative management of **Metro Málaga** services. The platform integrates user administration, remote file management via FTP, and SMTP communication, all built on a robust architecture synchronized in real-time through Sockets.



## 🚀 Key Features

## 🎨 Professional UI/UX
* **Adaptive Themes:** Includes a native **Dark Mode** and **Light Mode** toggle to improve user experience.
* **Multi-language Support:** Real-time translation between **Spanish (ES)** and **English (EN)** managed via database.
* **Modern Components:** Custom `TableCellRenderers` for interactive action buttons (Download, Edit, Delete).

## 📩 SMTP & FTP Integrated Services
* **Email Client:** Full inbox synchronization, message reading pane, and attachment support.
* **FTP Explorer:** Remote file management with real-time broadcast notifications via **TCP Sockets**.
* **Security:** Password protection using **BCrypt** hashing and Role-Based Access Control (RBAC).


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
