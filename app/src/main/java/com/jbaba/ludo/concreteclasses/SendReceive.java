package com.jbaba.ludo.concreteclasses;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class SendReceive implements Runnable, Serializable
{
    private transient Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public SendReceive(Socket socket)
    {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        try {
            Log.d("Debugging", "I enterred sendReceive run");
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream());
            Log.d("Debugging", "PrintWriter and bufferedReader are " + printWriter + " " + bufferedReader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }
}
