package org.example.network;

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

    public boolean addGroup(String name) {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("ADD_GROUP:" + name);
            String response = in.readLine();
            return "OK".equalsIgnoreCase(response);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGroup(String name) {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("DELETE_GROUP:" + name);
            String response = in.readLine();
            return "OK".equalsIgnoreCase(response);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroup(String oldName, String newName) {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("UPDATE_GROUP:" + oldName + "->" + newName);
            String response = in.readLine();
            return "OK".equalsIgnoreCase(response);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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
