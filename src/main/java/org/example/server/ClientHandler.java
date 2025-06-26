package org.example.server;

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
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String command = in.readLine();
            System.out.println("Отримано команду: " + command);

            GroupDao dao = new GroupDao();

            if ("GET_GROUPS".equalsIgnoreCase(command)) {
                List<String> groupNames = dao.getAllGroupNames();
                for (String name : groupNames) {
                    out.println(name);
                }
                out.println("END");

            } else if (command.startsWith("ADD_GROUP:")) {
                String name = command.substring("ADD_GROUP:".length()).trim();
                boolean success = dao.addGroup(name);
                out.println(success ? "OK" : "FAIL");

            } else if (command.startsWith("DELETE_GROUP:")) {
                String name = command.substring("DELETE_GROUP:".length()).trim();
                boolean success = dao.deleteGroupByName(name);
                out.println(success ? "OK" : "FAIL");

            } else if (command.startsWith("UPDATE_GROUP:")) {
                String[] parts = command.substring("UPDATE_GROUP:".length()).split("->");
                if (parts.length == 2) {
                    String oldName = parts[0].trim();
                    String newName = parts[1].trim();
                    boolean success = dao.updateGroupName(oldName, newName);
                    out.println(success ? "OK" : "FAIL");
                } else {
                    out.println("ERROR: Невірний формат команди");
                }

            } else {
                out.println("ERROR: Невідома команда");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
