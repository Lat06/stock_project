package org.example.server;

import org.example.ProductGroup;
import org.example.server.network.dao.GroupDao;
import org.example.server.network.dao.ProductDao;
import org.example.shared.model.Product;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            Object commandObj = in.readObject();
            if (!(commandObj instanceof String command)) {
                out.writeObject("ERROR: invalid command");
                return;
            }

            System.out.println("Отримано команду: " + command);
            GroupDao groupDao = new GroupDao();
            ProductDao productDao = new ProductDao();

            switch (command) {


                case "get_groups" -> {
                    try {
                        List<ProductGroup> groups = groupDao.getAllGroups();
                        out.writeObject(groups);
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject(new ArrayList<>());
                    }
                }


                case "add_group" -> {
                    try {
                        String name = (String) in.readObject();
                        String description = (String) in.readObject();
                        boolean success = groupDao.addGroup(name, description);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }

                case "update_group" -> {
                    try {
                        String oldName = (String) in.readObject();
                        String newName = (String) in.readObject();
                        String newDescription = (String) in.readObject();
                        boolean success = groupDao.updateGroup(oldName, newName, newDescription);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }

                case "delete_group" -> {
                    try {
                        String name = (String) in.readObject();
                        boolean success = groupDao.deleteGroup(name);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }


                case "get_products" -> {
                    try {
                        List<Product> products = productDao.getAllProducts();
                        out.writeObject(products);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        out.writeObject(new ArrayList<>());
                    }
                }

                case "add_product" -> {
                    try {
                        Product product = (Product) in.readObject();
                        boolean success = productDao.addProduct(product);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }

                case "edit_product" -> {
                    try {
                        Product product = (Product) in.readObject();
                        boolean success = productDao.editProduct(product);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }

                case "delete_product" -> {
                    try {
                        int id = (Integer) in.readObject();
                        boolean success = productDao.deleteProduct(id);
                        out.writeObject(success ? "OK" : "FAIL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("FAIL");
                    }
                }

                default -> {
                    out.writeObject("ERROR: Unknown command");
                }
            }

            out.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
