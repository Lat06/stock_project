package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.shared.model.Product;

public class ProductFormController {

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private ComboBox<ProductGroup> groupComboBox;

    private Product product;
    private boolean saved = false;

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            nameField.setText(product.getName());
            descriptionField.setText(product.getDescription());
            priceField.setText(String.valueOf(product.getPrice()));

            for (ProductGroup group : groupComboBox.getItems()) {
                if (group.getId() == product.getGroupId()) {
                    groupComboBox.getSelectionModel().select(group);
                    break;
                }
            }
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void onSave() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price;

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            // простий alert, можна замінити на showAlert
            System.err.println("Неправильна ціна");
            return;
        }

        ProductGroup selectedGroup = groupComboBox.getValue();
        if (selectedGroup == null) {
            System.err.println("Групу не вибрано");
            return;
        }

        if (product == null) {
            product = new Product(name, description, price, selectedGroup.getId());
        } else {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setGroupId(selectedGroup.getId());
        }

        saved = true;
        ((Stage) nameField.getScene().getWindow()).close();
    }

    @FXML
    private void onCancel() {
        saved = false;
        ((Stage) nameField.getScene().getWindow()).close();
    }

    public Product getProduct() {
        return product;
    }

    public void setGroups(java.util.List<ProductGroup> groups) {
        groupComboBox.getItems().setAll(groups);
    }
}
