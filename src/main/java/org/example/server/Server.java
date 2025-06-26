package org.example.server;

import org.example.server.network.dao.DbInitializer;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        DbInitializer.init();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущено на порту 12345");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
