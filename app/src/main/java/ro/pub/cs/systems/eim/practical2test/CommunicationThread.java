package ro.pub.cs.systems.eim.practical2test;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    // Constructor of the thread, which takes a ServerThread and a Socket as parameters
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    // run() method: The run method is the entry point for the thread when it starts executing.
    // It's responsible for reading data from the client, interacting with the server,
    // and sending a response back to the client.
    @Override
    public void run() {
        // It first checks whether the socket is null, and if so, it logs an error and returns.
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            // Create BufferedReader and PrintWriter instances for reading from and writing to the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            // Read the city and informationType values sent by the client
            String operator1 = bufferedReader.readLine();
            String operator2 = bufferedReader.readLine();
            String operation_type = bufferedReader.readLine();
            if (operator2 == null || operator1.isEmpty() || operator1 == null || operator2.isEmpty() ) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }


            // Send the information back to the client
            String result = null;
            if (Objects.equals(operation_type, "mul")) {
                Thread.sleep(2000);
                Integer int1 = Integer.parseInt(operator1) * Integer.parseInt(operator2);
                if ((Integer.parseInt(operator1) != 0 || Integer.parseInt(operator2) !=0) && int1 == 0)
                    result = "overflow";
                else
                    result = int1.toString();
            }
            if (Objects.equals(operation_type, "sum")) {
                Integer int1 = Integer.parseInt(operator1) + Integer.parseInt(operator2);
                if ((Integer.parseInt(operator1) != 0 && Integer.parseInt(operator2) !=0) && int1 == 0)
                    result = "overflow";
                else
                    result = int1.toString();
            }


            // Send the result back to the client
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}
