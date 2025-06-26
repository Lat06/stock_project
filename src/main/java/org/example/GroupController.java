package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.network.ClientSocketService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupController implements Initializable {

    @FXML private TableView<ProductGroup> groupTable;
    @FXML private TableColumn<ProductGroup, String> nameColumn;
    @FXML private TableColumn<ProductGroup, String> descriptionColumn;

    private final ObservableList<ProductGroup> groups = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        loadGroupsFromServer();
        groupTable.setItems(groups);
    }

    private void loadGroupsFromServer() {
        groups.clear();
        try {
            ClientSocketService client = new ClientSocketService("localhost", 5555);
            List<String> groupNames = client.getGroups();
            for (String name : groupNames) {
                groups.add(new ProductGroup(name, ""));
            }
        } catch (Exception e) {
            showAlert("Помилка з'єднання", "Не вдалося отримати список груп із сервера.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onAdd() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Нова група");
        dialog.setHeaderText("Введіть назву нової групи:");
        dialog.setContentText("Назва:");
        dialog.showAndWait().ifPresent(name -> {
            try {
                ClientSocketService client = new ClientSocketService("localhost", 5555);
                boolean success = client.addGroup(name);
                if (success) {
                    groups.add(new ProductGroup(name, ""));
                } else {
                    showAlert("Помилка", "Не вдалося додати групу на сервер.");
                }
            } catch (Exception e) {
                showAlert("Помилка", "Сталася помилка при з'єднанні з сервером.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onEdit() {
        ProductGroup selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog(selected.getName());
            dialog.setTitle("Редагування групи");
            dialog.setHeaderText("Змінити назву групи:");
            dialog.setContentText("Назва:");
            dialog.showAndWait().ifPresent(newName -> {
                try {
                    ClientSocketService client = new ClientSocketService("localhost", 5555);
                    boolean success = client.updateGroup(selected.getName(), newName);
                    if (success) {
                        selected.setName(newName);
                        groupTable.refresh();
                    } else {
                        showAlert("Помилка", "Не вдалося оновити назву групи.");
                    }
                } catch (Exception e) {
                    showAlert("Помилка", "Сталася помилка при з'єднанні з сервером.");
                    e.printStackTrace();
                }
            });
        }
    }


    @FXML
    private void onDelete() {
        ProductGroup selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClientSocketService client = new ClientSocketService("localhost", 5555);
            boolean success = client.deleteGroup(selected.getName());
            if (success) {
                groups.remove(selected);
            } else {
                showAlert("Помилка", "Не вдалося видалити групу з сервера.");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
