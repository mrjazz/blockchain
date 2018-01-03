package com.blockchain.sandbox.control;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by denis on 11/4/2017.
 */
public class WebServer {

//    public static void main(String argv[]) throws Exception {
//        ServerSocket welcomeSocket = new ServerSocket(8080);
//
//        while (true) {
//            Socket connectionSocket = welcomeSocket.accept();
//            Server server = new Server(connectionSocket.getInputStream(), connectionSocket.getOutputStream());
//            (new Thread(server)).start();
//        }
//    }
//
//}
//
//class Server implements Runnable {
//
//    private InputStream inputStream;
//    private OutputStream outputStream;
//
//    public Server(InputStream inputStream, OutputStream outputStream) {
//        this.inputStream = inputStream;
//        this.outputStream = outputStream;
//    }
//
//    @Override
//    public void run() {
//        BufferedReader inFromClient =
//                new BufferedReader(new InputStreamReader(inputStream));
//        DataOutputStream outToClient = new DataOutputStream(outputStream);
//        try {
//            String clientSentence = inFromClient.readLine();
//            System.out.println("Received: " + clientSentence);
//            outToClient.writeBytes(clientSentence);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
