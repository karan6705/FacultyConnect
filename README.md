<p align="center">
  <img src="src/main/resources/s25/cs151/application/controllers/images/logo1.png" alt="FacultyConnect Logo" width="120"/>
  <h1 align="center">🚀 <strong>FacultyConnect</strong></h1>
  <img src="https://img.shields.io/badge/Java-17-blue" alt="Java Version"/>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-brightgreen" alt="License"/></a>
</p>

<details>
  <summary>📖 <strong>Table of Contents</strong></summary>
  <ol>
    <li><a href="#features">✨ Features</a></li>
    <li><a href="#demo">🎬 Demo</a></li>
    <li><a href="#tech-stack">🛠️ Tech Stack</a></li>
    <li><a href="#prerequisites">🔧 Prerequisites</a></li>
    <li><a href="#installation">🚀 Installation</a></li>
    <li><a href="#configuration">⚙️ Configuration</a></li>
    <li><a href="#usage">🎯 Usage</a></li>
    <li><a href="#project-structure">📁 Project Structure</a></li>
    <li><a href="#contributing">🤝 Contributing</a></li>
    <li><a href="#license">📄 License</a></li>
    <li><a href="#contact">✉️ Contact</a></li>
  </ol>
</details>

---

## ✨ Features

* 🗓️ **Semester Management**: Define terms with custom start/end dates.
* 📚 **Course Catalog**: Add, edit, and remove courses within each semester.
* ⏰ **Time Slot Definition**: Customize office-hour slots by day, time range, and location.
* 📅 **Appointment Booking**: Book, modify, or cancel student appointments seamlessly.
* 🔍 **Schedule Views**: Searchable daily/weekly calendar by course or student.
* 💾 **Persistent Storage**: All data stored locally in an SQLite database.
* 🏗️ **MVC Architecture**: Clear separation of models, views (FXML), and controllers.

## 🎬 Demo

<p align="center">
  <img src="src/main/resources/s25/cs151/application/controllers/images/logo2.png" alt="FacultyConnect Screenshot" width="60%"/>
</p>

*Browse upcoming appointments and manage office hours with a clean, intuitive interface.*

---

## 🛠️ Tech Stack

| Component    | Technology             |
| ------------ | ---------------------- |
| Language     | Java 17                |
| UI Framework | JavaFX + FXML          |
| Build Tool   | Maven                  |
| Database     | SQLite (JDBC)          |
| IDE          | IntelliJ IDEA / VSCode |

---

## 🔧 Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven 3.6+

---

## 🚀 Installation

1. **Clone the repo**

   ```bash
   git clone https://github.com/karan6705/FacultyConnect.git
   cd FacultyConnect
   ```
2. **Build the project**

   ```bash
   mvn clean install
   ```
3. **Run the application**

   ```bash
   mvn javafx:run
   # or
   java -jar target/FacultyConnect-1.0.jar
   ```

---

## ⚙️ Configuration

* The `office_hours.db` file is auto-generated in the working directory.
* FXML files are in `src/main/resources/s25/cs151/application/controllers`.
* To adjust database settings, edit `Database.java` (`src/main/java/s25/cs151/application/storage/Database.java`).

---

## 🎯 Usage

1. **Define Semesters**: Go to **Manage Semesters** → **Add Semester** → Enter term details.
2. **Add Courses**: Navigate to **Course Catalog** → **New Course** → Fill code & title.
3. **Set Time Slots**: In **Office Hours**, click **Add Time Slot** → Choose days & times.
4. **Manage Appointments**: Under **Book Appointment**, input student info & pick a slot.
5. **View & Search**: Use **View Schedule** or **Search** to filter by course/student.

---

## 📁 Project Structure

```
FacultyConnect/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/s25/cs151/application/
│   │   │   ├── controllers/  # FXML controllers
│   │   │   ├── models/       # Business models
│   │   │   └── storage/      # DB utilities
│   │   └── resources/s25/cs151/application/controllers/  # FXML & assets
└── README.md
```

---

## 🤝 Contributing

1. Fork the repo
2. Create a branch: `git checkout -b feature/YourFeature`
3. Commit: `git commit -m "Add YourFeature"`
4. Push: `git push origin feature/YourFeature`
5. Open a Pull Request

We welcome feedback, issues, and contributions!

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for details.

---

## ✉️ Contact

👤 **Karan Raval** – [karanraval3409@gmail.com](mailto:karanraval3409@gmail.com)

Project Link: [https://github.com/karan6705/FacultyConnect](https://github.com/karan6705/FacultyConnect)
