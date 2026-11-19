package org.os.assignment_3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class HelloController {
    PriorityScheduling scheduler = new PriorityScheduling();

    @FXML
    private TextField processNameField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField arrivalTimeField;

    @FXML
    private TextField burstTimeField;

    @FXML
    private TextField priorityField;

    @FXML
    private TextField processNum;

    @FXML
    private TextField contextswitching;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private VBox ganttChartVBox;

    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, String> nameColumn;
    @FXML
    private TableColumn<Process, String> colorColumn;
    @FXML
    private TableColumn<Process, Integer> arrivalColumn;
    @FXML
    private TableColumn<Process, Integer> burstColumn;
    @FXML
    private TableColumn<Process, Integer> startColumn;
    @FXML
    private TableColumn<Process, Integer> completeColumn;
    @FXML
    private TableColumn<Process, Integer> priorityColumn;

    private ObservableList<Process> processList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind table columns to process fields
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getColor()));
        arrivalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAri_time()));
        burstColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBurst_time()));
        startColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStar_time()));
        completeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getComplete_time()));
        priorityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPriority()));

        processTable.setItems(processList);


        colorColumn.setCellFactory
                (column -> new TableCell<Process, String>() { // for convert color from Hexa to Square of color at the table
            @Override
            protected void updateItem(String color, boolean empty) {
                super.updateItem(color, empty);

                if (empty || color == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(20, 20);
                    rect.setFill(javafx.scene.paint.Color.web(color));
                    rect.setStroke(javafx.scene.paint.Color.WHITE);

                    setText(null);
                    setGraphic(rect);
                }
            }
        });
    }

    @FXML
    protected void onClickAddProcess() {
        try {
            String processName = processNameField.getText();
            String color = colorPicker.getValue().toString();
            int arrivalTime = Integer.parseInt(arrivalTimeField.getText());
            int burstTime = Integer.parseInt(burstTimeField.getText());
            int priority = Integer.parseInt(priorityField.getText());

            Process process = new Process(processName, color, arrivalTime, burstTime);
            process.setPriority(priority);

            scheduler.push(process);
            processList.add(process); // Add to table
            processNameField.clear();
            arrivalTimeField.clear();
            burstTimeField.clear();
            priorityField.clear();
        } catch (NumberFormatException e) {
            reportTextArea.setText("Error: Please enter valid numbers for Arrival Time, Burst Time, and Priority.");
        }
    }

    @FXML
    protected void onClickStartProcess() {
        try {
            int numProcesses = Integer.parseInt(processNum.getText());
            int context = Integer.parseInt(contextswitching.getText());

            List<Process> completedProcesses = scheduler.schedule(numProcesses, context);

            // Update table with start and completion times
            processList.clear();
            processList.addAll(completedProcesses);

            // Display report
            String report = scheduler.report(completedProcesses);
            reportTextArea.setText(report);

            // Draw Gantt Chart
            drawGanttChart(completedProcesses);
        } catch (NumberFormatException e) {
            reportTextArea.setText("Error: Please enter valid numbers for number of processes or context switching.");
        }
    }

    private void drawGanttChart(List<Process> completedProcesses) {
        ganttChartVBox.getChildren().clear();
        Pane pane = new Pane();

        double currentY = 50;
        double height = 50;

        for (Process process : completedProcesses) {
            double startX = process.getStar_time() * 10;
            double width = process.getBurst_time() * 10;

            // Draw process rectangle
            Rectangle rect = new Rectangle(startX, currentY, width, height);
            rect.setFill(javafx.scene.paint.Color.web(process.getColor()));

            // Add process name above the rectangle
            Label label = new Label(process.getName());
            label.setLayoutX(startX + width / 2 - 10);
            label.setLayoutY(currentY - 20);

            // Add timeline below the rectangle
            Label startTimeLabel = new Label(String.valueOf(process.getStar_time()));
            startTimeLabel.setLayoutX(startX - 5);
            startTimeLabel.setLayoutY(currentY + height + 5);

            Label endTimeLabel = new Label(String.valueOf(process.getComplete_time()));
            endTimeLabel.setLayoutX(startX + width - 5);
            endTimeLabel.setLayoutY(currentY + height + 5);

            pane.getChildren().addAll(rect, label, startTimeLabel, endTimeLabel);

            // Move to the next row for the next process
            currentY += height + 20;
        }

        // Draw timeline line
        Line timeline = new Line(0, currentY + 5, 1000, currentY + 5);
        timeline.setStroke(javafx.scene.paint.Color.GRAY);
        timeline.setStrokeWidth(2);
        pane.getChildren().add(timeline);

        ganttChartVBox.getChildren().add(pane);
    }
}