package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, String> manufacturerColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> groupColumn;

    private ObservableList<Product> products = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        manufacturerColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getManufacturer()));
        quantityColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPricePerUnit()).asObject());
        groupColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGroup().getName()));

        // Тестовий запис
        ProductGroup group = new ProductGroup("Продовольчі", "Харчові продукти");
        products.add(new Product("Гречка", "Крупа гречана", "Агро", 100, 25.5, group));

        productTable.setItems(products);
    }

    @FXML
    private void onAdd() {
        Product newProduct = new Product("", "", "", 0, 0.0, products.get(0).getGroup());
        if (showProductDialog(newProduct)) {
            products.add(newProduct);
        }
    }

    private boolean showProductDialog(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product-form.fxml"));
            DialogPane dialogPane = loader.load();

            ProductFormController controller = loader.getController();
            controller.setProduct(product);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Редагування товару");

            dialog.showAndWait();
            return controller.isSaved();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    private void onEdit() {
        System.out.println("Редагувати товар");
    }

    @FXML
    private void onDelete() {
        System.out.println("Видалити товар");
    }
}
