package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProductFormController {

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField manufacturerField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ComboBox<ProductGroup> groupComboBox;

    private Product product;
    private boolean saved = false;

    public void setProduct(Product product) {
        this.product = product;

        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        manufacturerField.setText(product.getManufacturer());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPricePerUnit()));
        groupComboBox.getItems().add(product.getGroup());
        groupComboBox.setValue(product.getGroup());
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void onSave() {
        try {
            product.setName(nameField.getText());
            product.setDescription(descriptionField.getText());
            product.setManufacturer(manufacturerField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            product.setPricePerUnit(Double.parseDouble(priceField.getText()));
            product.setGroup(groupComboBox.getValue());

            saved = true;
            ((Stage) nameField.getScene().getWindow()).close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Некоректні дані");
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}
