package org.example.network;

import org.example.ProductGroup;
import org.example.shared.model.Product;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSocketService {
    private final String host;
    private final int port;

    public ClientSocketService(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public List<ProductGroup> getGroupObjects() {
        List<ProductGroup> result = new ArrayList<>();
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("get_groups");
            out.flush();

            Object response = in.readObject();
            if (response instanceof List<?> list) {
                for (Object obj : list) {
                    if (obj instanceof ProductGroup group) {
                        result.add(group);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addGroup(String name, String description) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("add_group");
            out.writeObject(name);
            out.writeObject(description);
            out.flush();

            Object response = in.readObject();
            return "OK".equals(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroup(String oldName, String newName, String newDescription) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("update_group");
            out.writeObject(oldName);
            out.writeObject(newName);
            out.writeObject(newDescription);
            out.flush();

            Object response = in.readObject();
            return "OK".equals(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGroup(String name) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("delete_group");
            out.writeObject(name);
            out.flush();

            Object response = in.readObject();
            return "OK".equals(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<Product> getProducts() {
        List<Product> result = new ArrayList<>();
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("get_products");
            out.flush();

            Object response = in.readObject();
            if (response instanceof List<?> list) {
                for (Object obj : list) {
                    if (obj instanceof Product product) {
                        result.add(product);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addProduct(Product product) {
        return sendProduct("add_product", product);
    }

    public boolean editProduct(Product product) {
        return sendProduct("edit_product", product);
    }

    public boolean deleteProduct(int id) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("delete_product");
            out.writeObject(id);
            out.flush();

            Object response = in.readObject();
            return "OK".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendProduct(String command, Product product) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(command);
            out.writeObject(product);
            out.flush();

            Object response = in.readObject();
            return "OK".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
