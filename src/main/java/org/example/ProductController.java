package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.network.ClientSocketService;
import org.example.shared.model.Product;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> groupIdColumn;

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private ComboBox<ProductGroup> groupComboBox;

    private final ClientSocketService service = new ClientSocketService("localhost", 12345);
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<ProductGroup> groups = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        priceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPrice()));
        groupIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getGroupId()));

        productTable.setItems(products);
        groupComboBox.setItems(groups);

        loadGroups();
        loadProducts();

        productTable.setOnMouseClicked(this::onSelect);
    }

    private void loadGroups() {
        groups.setAll(service.getGroupObjects());
    }

    private void loadProducts() {
        products.setAll(service.getProducts());
    }

    public void onAdd() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        ProductGroup group = groupComboBox.getSelectionModel().getSelectedItem();

        if (group == null) {
            showAlert("Оберіть групу!");
            return;
        }

        Product product = new Product(name, description, price, group.getId());
        boolean success = service.addProduct(product);
        if (success) {
            loadProducts();
            clearFields();
        } else {
            showAlert("Помилка при додаванні товару.");
        }
    }

    public void onEdit() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Оберіть товар!");
            return;
        }

        String name = nameField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        ProductGroup group = groupComboBox.getSelectionModel().getSelectedItem();

        if (group == null) {
            showAlert("Оберіть групу!");
            return;
        }

        selected.setName(name);
        selected.setDescription(description);
        selected.setPrice(price);
        selected.setGroupId(group.getId());

        boolean success = service.editProduct(selected);
        if (success) {
            loadProducts();
            clearFields();
        } else {
            showAlert("Помилка при редагуванні товару.");
        }
    }

    public void onDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Оберіть товар!");
            return;
        }

        boolean success = service.deleteProduct(selected.getId());
        if (success) {
            loadProducts();
            clearFields();
        } else {
            showAlert("Помилка при видаленні товару.");
        }
    }

    private void onSelect(MouseEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nameField.setText(selected.getName());
            descriptionField.setText(selected.getDescription());
            priceField.setText(String.valueOf(selected.getPrice()));
            groupComboBox.getSelectionModel().select(
                    groups.stream().filter(g -> g.getId() == selected.getGroupId()).findFirst().orElse(null)
            );
        }
    }

    private void clearFields() {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        groupComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
