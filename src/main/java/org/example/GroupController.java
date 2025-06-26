package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.network.ClientSocketService;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


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

        groupTable.setItems(groups);
        loadGroupsFromServer();
    }

    private void loadGroupsFromServer() {
        Task<List<ProductGroup>> loadTask = new Task<>() {
            @Override
            protected List<ProductGroup> call() throws Exception {
                ClientSocketService client = new ClientSocketService("localhost", 5555);
                return client.getGroupObjects(); // Повертає List<ProductGroup>
            }
        };

        loadTask.setOnSucceeded(event -> {
            groups.clear();
            groups.addAll(loadTask.getValue());
        });

        loadTask.setOnFailed(event -> {
            showAlert("Помилка з'єднання", "Не вдалося отримати список груп із сервера.");
            loadTask.getException().printStackTrace();
        });

        new Thread(loadTask).start();
    }

    @FXML
    private void onAdd() {
        Dialog<ProductGroup> dialog = createGroupDialog(null);
        dialog.setTitle("Нова група");

        dialog.showAndWait().ifPresent(newGroup -> {
            Task<Boolean> addTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    ClientSocketService client = new ClientSocketService("localhost", 5555);
                    return client.addGroup(newGroup.getName(), newGroup.getDescription());
                }
            };

            addTask.setOnSucceeded(e -> {
                if (addTask.getValue()) {
                    groups.add(newGroup);
                } else {
                    showAlert("Помилка", "Не вдалося додати групу.");
                }
            });

            addTask.setOnFailed(e -> {
                showAlert("Помилка", "Сталася помилка при з'єднанні з сервером.");
                addTask.getException().printStackTrace();
            });

            new Thread(addTask).start();
        });
    }

    @FXML
    private void onEdit() {
        ProductGroup selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Увага", "Оберіть групу для редагування.");
            return;
        }

        Dialog<ProductGroup> dialog = createGroupDialog(selected);
        dialog.setTitle("Редагування групи");

        dialog.showAndWait().ifPresent(editedGroup -> {
            Task<Boolean> updateTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    ClientSocketService client = new ClientSocketService("localhost", 5555);
                    return client.updateGroup(selected.getName(), editedGroup.getName(), editedGroup.getDescription());
                }
            };

            updateTask.setOnSucceeded(e -> {
                if (updateTask.getValue()) {
                    selected.setName(editedGroup.getName());
                    selected.setDescription(editedGroup.getDescription());
                    groupTable.refresh();
                } else {
                    showAlert("Помилка", "Не вдалося оновити групу.");
                }
            });

            updateTask.setOnFailed(e -> {
                showAlert("Помилка", "Сталася помилка при з'єднанні з сервером.");
                updateTask.getException().printStackTrace();
            });

            new Thread(updateTask).start();
        });
    }

    @FXML
    private void onDelete() {
        ProductGroup selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Увага", "Оберіть групу для видалення.");
            return;
        }

        Task<Boolean> deleteTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                ClientSocketService client = new ClientSocketService("localhost", 5555);
                return client.deleteGroup(selected.getName()); // Передаємо тільки name
            }
        };

        deleteTask.setOnSucceeded(e -> {
            if (deleteTask.getValue()) {
                groups.remove(selected);
            } else {
                showAlert("Помилка", "Не вдалося видалити групу.");
            }
        });

        deleteTask.setOnFailed(e -> {
            showAlert("Помилка", "Помилка при з'єднанні з сервером.");
            deleteTask.getException().printStackTrace();
        });

        new Thread(deleteTask).start();
    }

    private Dialog<ProductGroup> createGroupDialog(ProductGroup group) {
        Dialog<ProductGroup> dialog = new Dialog<>();
        dialog.setHeaderText("Введіть дані групи");

        Label nameLabel = new Label("Назва:");
        TextField nameField = new TextField();
        Label descLabel = new Label("Опис:");
        TextField descField = new TextField();

        if (group != null) {
            nameField.setText(group.getName());
            descField.setText(group.getDescription());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(descLabel, 0, 1);
        grid.add(descField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new ProductGroup(nameField.getText(), descField.getText());
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
