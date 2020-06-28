package com.ersek.opensesame;

import android.os.AsyncTask;

import java.io.FileInputStream;   //deschidere fisier si citit din el
import java.io.IOException;       //eroare la deschidere(fara drepturi, fisier neexistent)
import java.io.InputStream;       //pentru accesare continut fisier/socket
import java.io.OutputStream;      //pentru scriere in fisier/socket
import java.net.Socket;           //pentru utilizare socketuri

public class ImageSender extends AsyncTask<Integer, Integer, Integer> {
    private String filename;

    ImageSender(String filename) {
        this.filename = filename; }
    //constructorul pentru crearea clasei

    private byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }     //functia pentru convertire int in bytearray

    private void sendImage() throws IOException {
        String TCP_IP = "192.168.0.17";
        int TCP_PORT = 1251;
        Socket socket = new Socket(TCP_IP, TCP_PORT);   //creare socket
        InputStream inputStream = new FileInputStream(filename); //deschiderea fisierului
        OutputStream outputStream = socket.getOutputStream();    //accesarea canalului de output a socketului
        InputStream responseStream = socket.getInputStream();    //accesarea canalului de input a socketului
        int BUFFER_SIZE = 64;        // lungimea fiecarui buffer
        byte[] buffer = new byte[BUFFER_SIZE];  //scrierea in format byte a fiecarui buffer
        byte[] response = new byte[4]; //raspunsul

        int currentBufferSize = inputStream.read(buffer);   //citeste din fisier si salveaza cat a citit
        while (currentBufferSize != -1) { ///-1 este sfarsit de fisier

            outputStream.write(intToByteArray(currentBufferSize));  //trimite catre controller CBS
            outputStream.write(buffer); //trimite Bufferul propriu zis
            currentBufferSize = inputStream.read(buffer);   //continua citirea
        }

        outputStream.write(intToByteArray(-1));  ///trimite -1 ca sfarsit de fisier
        responseStream.read(response);   //primeste raspunsu de la cotroller
        int ACCESS_GRANTED_DOOR_OPEN = 1;
        int ACCESS_GRANTED_DOOR_CLOSED = 2;
        MainActivity.SAME_PERSON = response[3] == ACCESS_GRANTED_DOOR_CLOSED
                || response[3] == ACCESS_GRANTED_DOOR_OPEN;

        inputStream.close();  //inchidere toate conexiunile
        outputStream.close();
        responseStream.close();

        socket.close();
    }

    @Override
    protected Integer doInBackground(Integer... integers) { //metoda suprascrisa din Asynctask
        try {
            sendImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}