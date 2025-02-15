package com.liqaa.server.controllers.FXMLcontrollers;

import com.liqaa.server.Main;
import com.liqaa.server.controllers.services.implementations.MessageServiceImpl;
import com.liqaa.server.controllers.services.implementations.UserServicesImpl;
import com.liqaa.server.util.FilePaths;
import com.liqaa.server.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatisticsController {

    @FXML
    private Label countriesLabel;
    @FXML
    private StackedBarChart<String, Number> dailyEntriesChart;
    @FXML
    private NumberAxis dailyEntriesYAxis;
    @FXML
    private Text femaleText;
    @FXML
    private Text maleText;
    @FXML
    private Label offlineUserLabel;
    @FXML
    private Label onlineUserLabel;
    @FXML
    private StackedBarChart<String, Number> topCountriesChart;
    @FXML
    private NumberAxis topCountriesChartYAxis;
    @FXML
    private Label totalUserLabel;

    @FXML
    private Pane announcementPane;
    @FXML
    private Pane logoutPane;
    @FXML
    private Pane managerPane;

    public void initialize() {
        initializePlaceholderChartData();
        topCountriesChartYAxis.setLabel("Number of Users");
        dailyEntriesYAxis.setLabel("Number of Messages");
        dailyEntriesChart.setLegendVisible(false);
    }

    @FXML
    public void logout(MouseEvent event) {
        SceneManager.getInstance().showLogin();
    }

    @FXML
    public void switchToAnnouncements(MouseEvent event) {
        SceneManager.getInstance().showAnnouncements();
    }

    @FXML
    public void switchToManager(MouseEvent event) {
        SceneManager.getInstance().showManager();
    }

    private void initializePlaceholderChartData() {
        ObservableList<XYChart.Series<String, Number>> data = topCountriesChart.getData();
        if (data == null) {
            data = FXCollections.observableArrayList();
            topCountriesChart.setData(data);
        }
        XYChart.Series<String, Number> placeholderSeries = new XYChart.Series<>();
        placeholderSeries.getData().add(new XYChart.Data<>("Loading", 0));
        data.add(placeholderSeries);

        ObservableList<XYChart.Series<String, Number>> data2 = dailyEntriesChart.getData();
        if (data2 == null) {
            data2 = FXCollections.observableArrayList();
            dailyEntriesChart.setData(data2);
        }
        XYChart.Series<String, Number> placeholderSeries2 = new XYChart.Series<>();
        placeholderSeries2.getData().add(new XYChart.Data<>("Loading", 0));
        data2.add(placeholderSeries2);
    }

    public void updateAllData() {
        int totalUsers = UserServicesImpl.getInstance().getNumberAllUsers();
        int onlineUsers = UserServicesImpl.getInstance().getNumberAllOnlineUsers();
        int offlineUsers = UserServicesImpl.getInstance().getNumberAllOfflineUsers();
        int numberOfCountries = UserServicesImpl.getInstance().getNumberAllCountryOfUsers();

        double malePercentage = totalUsers == 0 ? 0 : (UserServicesImpl.getInstance().getNumberAllMaleUsers() / (double) totalUsers) * 100;
        double femalePercentage = totalUsers == 0 ? 0 : (UserServicesImpl.getInstance().getNumberAllFemaleUsers() / (double) totalUsers) * 100;

        Map<String, Integer> topCountriesData = UserServicesImpl.getInstance().getTopCountriesOfUsers();
        Map<String, Integer> messagesPerDay = MessageServiceImpl.getInstance().getMessagesPerDay();

        totalUserLabel.setText(String.valueOf(totalUsers));
        onlineUserLabel.setText(String.valueOf(onlineUsers));
        offlineUserLabel.setText(String.valueOf(offlineUsers));
        countriesLabel.setText(String.valueOf(numberOfCountries));
        maleText.setText(String.format("%.2f%%", malePercentage));
        femaleText.setText(String.format("%.2f%%", femalePercentage));

        updateChart(topCountriesChart, topCountriesData, "Users");
        updateDailyEntriesChart(messagesPerDay);
        setBarColorToBlue();
    }

    private void updateChart(StackedBarChart<String, Number> chart, Map<String, Integer> data, String seriesName) {
        ObservableList<XYChart.Series<String, Number>> chartData = chart.getData();
        chartData.clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        data.forEach((key, value) -> {
            if (value != null && key != null) {
                series.getData().add(new XYChart.Data<>(key, value));
            }
        });
        chartData.add(series);
    }

    private void updateDailyEntriesChart(Map<String, Integer> actualData) {
        Map<String, Integer> messagesPerDay = initializeDaysOfWeek();
        actualData.forEach((key, value) -> messagesPerDay.put(key, value));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        messagesPerDay.forEach((day, count) -> {
            if (count != null && day != null) {
                series.getData().add(new XYChart.Data<>(day, count));
            }
        });

        ObservableList<XYChart.Series<String, Number>> chartData = dailyEntriesChart.getData();
        chartData.clear();
        chartData.add(series);
    }

    private Map<String, Integer> initializeDaysOfWeek() {
        Map<String, Integer> daysOfWeek = new LinkedHashMap<>();
        daysOfWeek.put("Monday", 0);
        daysOfWeek.put("Tuesday", 0);
        daysOfWeek.put("Wednesday", 0);
        daysOfWeek.put("Thursday", 0);
        daysOfWeek.put("Friday", 0);
        daysOfWeek.put("Saturday", 0);
        daysOfWeek.put("Sunday", 0);
        return daysOfWeek;
    }

    private void setBarColorToBlue() {
        for (XYChart.Series<String, Number> series : topCountriesChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #4A90E2;");
                }
            }
        }

        for (XYChart.Series<String, Number> series : dailyEntriesChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #4A90E2;");
                }
            }
        }
    }
}