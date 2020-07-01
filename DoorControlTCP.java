package com.ersek.opensesame;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DoorControlTCP extends AsyncTask<Integer, Integer, Integer>{


    public void communicate(byte code) throws IOException {
        int CODE_CHECK_STATUS = 1;
        int CODE_OPEN_DOOR = 2;
        int DOOR_CLOSE = 1;
        int DOOR_OPEN = 2;

        String TCP_IP = "192.168.0.17";
        int TCP_PORT = 1252;
        Socket socket = new Socket(TCP_IP, TCP_PORT);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] response = new byte[1];
        byte[] codes = new byte[1];
        codes[0] = code;
        outputStream.write(codes);
        if (code == CODE_CHECK_STATUS) {
            inputStream.read(response);
            if (response[0] == DOOR_CLOSE) {
                DoorController.OPEN_DOOR = false;
            } else if (response[0] == DOOR_OPEN) {
                DoorController.OPEN_DOOR = true;
            }
        }
        socket.close();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            communicate(integers[0].byteValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
