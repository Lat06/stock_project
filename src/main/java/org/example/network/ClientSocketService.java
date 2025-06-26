package org.example.network;

import org.example.ProductGroup;

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

    // Старий варіант: додає групу без опису
    public boolean addGroup(String name) {
        return addGroup(name, "");
    }

    // Новий варіант: додає групу з описом
    public boolean addGroup(String name, String description) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("add_group");
            out.writeObject(name);
            out.writeObject(description);

            Object response = in.readObject();
            return response instanceof String s && s.equals("OK");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Отримання повного списку груп (назва + опис)
    public List<ProductGroup> getGroupObjects() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("get_groups");

            Object response = in.readObject();
            if (response instanceof List<?> list && (list.isEmpty() || list.get(0) instanceof ProductGroup)) {
                return (List<ProductGroup>) list;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    // Оновлення назви та опису групи
    public boolean updateGroup(String oldName, String newName, String newDescription) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("update_group");
            out.writeObject(oldName);
            out.writeObject(newName);
            out.writeObject(newDescription);

            Object response = in.readObject();
            return response instanceof String s && s.equals("OK");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Видалення групи по імені (залишається без змін)
    public boolean deleteGroup(String name) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("delete_group");
            out.writeObject(name);

            Object response = in.readObject();
            return response instanceof String s && s.equals("OK");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Старий метод: лише назви
    public List<String> getGroups() {
        List<String> groups = new ArrayList<>();

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("GET_GROUPS");

            String line;
            while ((line = in.readLine()) != null) {
                if ("END".equals(line)) break;
                groups.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return groups;
    }
}
