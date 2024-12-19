## 📚 **Project Overview**

The **OS Project** is a **standalone Java client-server application**. It features a **backend** for processing images and managing data, and a **frontend** built with **JavaFX** for the graphical user interface.

The main functionalities of this project include:

- **Image Loading**: Support for various image formats (PBM, PGM, PPM) to process and convert images.
- **File Conversion**: Ability to save and convert images into multiple file formats.
- **Graphical User Interface (GUI)**: User-friendly, interactive UI for seamless user experience.

This project follows a **modular structure** with clear separation between **frontend** and **backend** components, enabling scalability and maintainability.

---

# 🌐 **Translation (I18N) Support**

The application supports **I18N (Internationalization)** to provide a multilingual user experience. Using the **I18N module**, the user interface (UI) can be displayed in different languages based on the user's preferences or system locale.

### **Supported Languages**

The application currently supports the following languages:

- 🇺🇸 **English (en)**
- 🇮🇹 **Italian (it)**
- 🇫🇷 **French (fr)**
- 🇩🇪 **Deutsch (de)**

### **How it Works**

1. **Translation Files**: Each supported language has a dedicated **properties file** (e.g., `messages_en.properties`, `messages_it.properties`).
2. **Cold Language Switching**: The user can switch the application language by restarting the app.
3. **Last Language used**: save in `user.prefs file` .

The **I18N system** ensures that all UI elements, error messages, and system prompts are displayed in the correct language.

---

# 🖼️ **Available Filters and Transformations**

| **Filter/Transformation** | **Description**                  |
| ------------------------- | -------------------------------- |
| **Grayscale**             | Converts the image to grayscale. |
| **Rotate Right**          | Rotate right of 90 degrees.      |
| **Rotate Left**           | Rotate left of 90 degrees.       |
| **Flip Horizontally**     | Flips the image horizontally.    |
| **Flip Vertically**       | Flips the image vertically.      |

---

# 📊 **Status Bar Communication**

The **status bar** provides real-time feedback on system events.

---

## 📂 **Project Structure**

```plaintext
os/
├── backend/                 # Backend logic and server-side operations
│   ├── src/                 # Source code for backend
│   ├── pom.xml              # Maven configuration file for backend dependencies
│   └── target/              # Compiled classes and build artifacts
│
├── frontend/                # Frontend logic and GUI
│   ├── src/                 # Source code for frontend
│   ├── pom.xml              # Maven configuration file for frontend dependencies
│   └── target/              # Compiled classes and build artifacts
│
├── .gitignore               # List of files and folders to ignore in Git
├── .gitlab-ci.yml           # GitLab CI/CD configuration file
└── README.md                # Project documentation file (this file)
```

---

## 🔍 **System Architecture**

The system follows a client-server architecture where the frontend serves as the client and the backend serves as the server. The frontend GUI interacts with the backend to send requests for image processing and conversion.

          +-------------------+        +------------------+
          |                   |        |                  |
          |     Frontend      | <----> |     Backend      |
          |  (JavaFX + GUI)   |        |  (Standalone)    |
          |                   |        |  (Pure Java)     |
          +-------------------+        +------------------+

---

## ⚙️ **Backend Overview**

The **backend** handles the processing logic and acts as the **server-side** of the application. It processes image files, manages conversions, and sends responses to the frontend when requested.

### **Main Features**

- **Image Loading**: Handles the loading of image files in PBM, PGM, and PPM formats.
- **Image Conversion**: Converts images to other formats (like PBM, PGM, PPM) using internal handlers.
- **File Handling**: Manages saving and loading images from disk.

## 💻 **Frontend Overview**

The **frontend** is a graphical user interface (GUI) built using JavaFX. It allows users to upload images, visualize them, and interact with the backend for image processing and conversion.

### **Main Features**

- **Interactive User Interface**: Allows users to load, view, and convert image files.
- **Image Upload**: Users can upload files and view them directly in the application.
- **File Conversion**: Users can convert image files into PBM, PGM, and PPM formats.

---

## 📃 **CI/CD Pipeline**

The project uses **GitLab CI/CD** to automate the testing and deployment process. The `.gitlab-ci.yml` file defines the steps required to build and deploy the application automatically when changes are pushed to the repository.

---

### 🛠️ **Installation Guide**

To set up and run the **2Dimageeditor** on your system, follow the instructions below:

---

#### **Prerequisites**

Ensure you have the following software installed on your system:

1. **Java Development Kit (JDK)** - Version 11 or higher.  
   [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

2. **Apache Maven** - Used for building the project.  
   [Download Maven](https://maven.apache.org/download.cgi)

3. **Git** - For cloning the repository.  
   [Download Git](https://git-scm.com/downloads)

---

#### **Installation Steps**

1. **Clone the Repository**  
   Open a terminal and execute the following command to clone the project repository:  
   ```bash
   git clone https://gitlab.com/your-repository-link/os-project.git
   cd os-project
   ```

2. **Build the Backend Module**  
   Navigate to the backend directory and build the module using Maven:  
   ```bash
   cd backend
   mvn clean install
   cd ..
   ```

3. **Build the Frontend Module**  
   Navigate to the frontend directory and build the module using Maven:  
   ```bash
   cd frontend
   mvn clean install
   cd ..
   ```

4. **Run the Application**  
   - Start the **backend server**:  
     ```bash
     java -jar backend/target/backend.jar
     ```  
   - Start the **frontend client**:  
     ```bash
     java -jar frontend/target/frontend.jar
     ```

5. **Access the Application**  
   Once both the backend and frontend are running, the GUI will launch automatically, allowing you to start using the application.

---

#### **Environment Variables**

If required, you can configure environment variables to customize paths or settings for the application. These variables can be set in your terminal or system settings:

- **`APP_CONFIG_PATH`**: Path to the application configuration file.
- **`USER_PREFS_FILE`**: Path to save user preferences (e.g., selected language).

Example (Linux/Mac):  
```bash
export APP_CONFIG_PATH=/path/to/config
export USER_PREFS_FILE=/path/to/user.prefs
```

Example (Windows - Command Prompt):  
```cmd
set APP_CONFIG_PATH=C:\path\to\config
set USER_PREFS_FILE=C:\path\to\user.prefs
```

---

#### **Troubleshooting**

- Ensure all dependencies (JDK, Maven) are correctly installed and added to your system's PATH.
- Verify that the backend is running before starting the frontend.
- Check for compilation errors by running `mvn clean install` in both the `backend` and `frontend` directories.
- Review the `logs/` directory for error messages if something goes wrong.

