package s25.cs151.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import s25.cs151.application.view.SceneManager;

public class HomeController {

    @FXML private Button viewScheduleButton;
    @FXML private Button editOfficeHoursButton;
    @FXML private Button editCoursesButton;
    @FXML private Button editTimeSlotsButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button searchScheduleButton;
    @FXML private Button editApptsButton;  // new

    @FXML
    private void initialize() { // Method to initialize all buttons on home page of application
        viewScheduleButton.setOnAction(this::handleViewSchedule);
        editOfficeHoursButton.setOnAction(this::handleEditOfficeHours);
        editCoursesButton.setOnAction(this::handleEditCourses);
        editTimeSlotsButton.setOnAction(this::handleEditTimeSlots);
        addAppointmentButton.setOnAction(this::handleAddAppointment);
        searchScheduleButton.setOnAction(this::handleSearchSchedule);
        editApptsButton.setOnAction(this::handleEditAppointments);
    }

    public void handleViewSchedule(ActionEvent event) { // Method to transition home page to view schedule page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/ViewSchedule.fxml",
                600, 400);
    }

    public void handleEditOfficeHours(ActionEvent event) {  // Method to transition home page to edit office hours page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/OfficeHoursManager.fxml",
                600, 400);
    }

    public void handleEditCourses(ActionEvent event) {  // Method to transition home page to edit courses page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/DefineCourses.fxml",
                600, 400);
    }

    public void handleEditTimeSlots(ActionEvent event) {    // Method to transition home page to edit time slots page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/TimeSlotView.fxml",
                600, 400);
    }

    public void handleAddAppointment(ActionEvent event) {   // Method to transition home page to add appointment page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/OfficeHoursSchedule.fxml",
                600, 400);
    }

    public void handleSearchSchedule(ActionEvent event) {   // Method to transition home page to search schedule page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/SearchSchedule.fxml",
                600, 400);
    }

    // NEW: navigates to the ManageAppointments page
    public void handleEditAppointments(ActionEvent event) { // Method to transition home page to edit appointments page
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/ManageAppointments.fxml",
                600, 400);
    }
}