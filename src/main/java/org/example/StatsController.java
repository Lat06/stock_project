package org.example;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.network.ClientSocketService;
import org.example.shared.model.ProductGroupStats;

import java.util.List;

public class StatsController {

    @FXML private TableView<ProductGroupStats> statsTable;
    @FXML private TableColumn<ProductGroupStats, String> groupColumn;
    @FXML private TableColumn<ProductGroupStats, Integer> countColumn;
    @FXML private TableColumn<ProductGroupStats, Double> sumColumn;
    @FXML private TableColumn<ProductGroupStats, Double> avgColumn;

    private final ClientSocketService socketService = new ClientSocketService("localhost", 1234);

    @FXML
    public void initialize() {
        groupColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGroupName()));
        countColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getProductCount()).asObject());
        sumColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject());
        avgColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAveragePrice()).asObject());

        loadStats();
    }

    @FXML
    private void onRefresh() {
        loadStats();
    }

    private void loadStats() {
        List<ProductGroupStats> stats = socketService.getStats();
        statsTable.setItems(FXCollections.observableArrayList(stats));
    }
}
