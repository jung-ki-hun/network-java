package com.nhnacademy.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("bye");
            Socket socket = serverSocket.accept();
            System.out.println("hi");
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            OutputStream out = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

            String line = null;
            List<String> result = new ArrayList<>();
            while ((line = br.readLine()) != null){
                result.add(line);
//                System.out.println(result);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server s = new Server();
        s.connect();
    }
}
