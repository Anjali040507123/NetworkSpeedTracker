# Network Speed Tracker

Network Speed Tracker is a JavaFX desktop application that runs the Ookla Speedtest CLI, displays ping/download/upload results, saves each test to MySQL, and provides a history table and line chart.

## Features

- Run an internet speed test from the desktop UI.
- Display ping in milliseconds and download/upload speeds in Mbps.
- Save test results with a timestamp in MySQL.
- View the latest 10, 20, 50, or 100 records.
- Compare download, upload, and ping values in a line chart.

## Requirements

- Java Development Kit (JDK) 17.
- JavaFX SDK 17.0.17.
- MySQL Server running locally on port `3306`.
- MySQL Connector/J.
- `org.json` JAR.
- Ookla Speedtest CLI available as `speedtest` on the system `PATH`.
- Eclipse IDE, or another Java IDE configured with the same libraries.

## Database Setup

Create the database and table before launching the application:

```sql
CREATE DATABASE network_speed_tracker;

USE network_speed_tracker;

CREATE TABLE speed_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ping DOUBLE NOT NULL,
    download DOUBLE NOT NULL,
    upload DOUBLE NOT NULL,
    test_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

The application currently connects with the MySQL user `root` and a password defined directly in `MainController.java`, `HistoryController.java`, and `DatabaseManager.java`. Update those values before running the application, and avoid committing real credentials to source control.

## Eclipse Setup

1. Import `NetworkSpeedTracking` as an existing Eclipse project.
2. Configure the project to use JDK 17.
3. Add the JavaFX SDK JARs from its `lib` directory to the project build path.
4. Add the MySQL Connector/J JAR and the `org.json` JAR to the build path.
5. Ensure `src` and `resources` are source folders. The FXML files must be available on the runtime classpath.
6. Update the absolute JAR paths in `.classpath` if the libraries are installed in different locations.

## Running

Before starting the application, verify that MySQL is running and that this command works from a terminal:

```text
speedtest --accept-license --accept-gdpr -f json
```

Run `com.network.speedtracking.Main` as a Java application from Eclipse. The main window provides:

- **Start Test**: runs Speedtest, updates the displayed values, and saves the result.
- **View History**: opens the history table and chart.

If JavaFX is not configured as a module, add the JavaFX SDK `lib` directory to the runtime path as required by the local Eclipse configuration.

## Project Structure

```text
NetworkSpeedTracking/
├── resources/
│   ├── history_view.fxml
│   └── main_view.fxml
└── src/
    ├── com/network/speedtracking/
    │   ├── DatabaseManager.java
    │   ├── HistoryController.java
    │   ├── Main.java
    │   ├── MainController.java
    │   ├── SpeedTestResult.java
    │   ├── SpeedTestService.java
    │   └── UploadServer.java
    └── models/SpeedRecord.java
```

## Troubleshooting

- **Speedtest failed**: confirm the Ookla CLI is installed and available on `PATH`, then accept its license/GDPR prompts.
- **Database connection failed**: check that MySQL is running, the `network_speed_tracker` database exists, and the credentials in the Java source are correct.
- **FXML or JavaFX errors**: confirm JavaFX JARs are on both the compile-time and runtime classpaths, and that `resources` is included as a source folder.
- **Missing JSON classes**: add the `org.json` JAR to the project build path.

## Notes

The current project uses Eclipse classpath entries with machine-specific absolute paths. For easier sharing, those dependencies should eventually be moved to Maven or Gradle and database credentials should be externalized through environment variables or a configuration file.