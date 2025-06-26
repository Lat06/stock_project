package org.example.server;

import org.example.ProductGroup;
import org.example.server.network.dao.GroupDao;

import java.io.*;
import java.net.Socket;
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
            GroupDao dao = new GroupDao();

            switch (command) {
                case "get_groups" -> {
                    List<ProductGroup> groups = dao.getAllGroups();
                    out.writeObject(groups);
                    out.flush();
                }

                case "add_group" -> {
                    String name = (String) in.readObject();
                    String description = (String) in.readObject();
                    boolean success = dao.addGroup(name, description);
                    out.writeObject(success ? "OK" : "FAIL");
                    out.flush();
                }

                case "update_group" -> {
                    String oldName = (String) in.readObject();
                    String newName = (String) in.readObject();
                    String newDescription = (String) in.readObject();
                    boolean success = dao.updateGroup(oldName, newName, newDescription);
                    out.writeObject(success ? "OK" : "FAIL");
                    out.flush();
                }

                case "delete_group" -> {
                    String name = (String) in.readObject();
                    boolean success = dao.deleteGroupByName(name);
                    out.writeObject(success ? "OK" : "FAIL");
                    out.flush();
                }

                default -> {
                    out.writeObject("ERROR: Unknown command");
                    out.flush();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
