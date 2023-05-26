package ro.pub.cs.systems.eim.practical2test;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String operator1;

    private final String operator2;

    private final String operation_type;
//    private final String informationType;
    private final TextView resultView;

    private Socket socket;

    public ClientThread(String address, int port, String operator1, String operator2, String operation_type, TextView resultView) {
        this.address = address;
        this.port = port;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.operation_type = operation_type;
        this.resultView = resultView;
    }

    @Override
    public void run() {
        try {
            // tries to establish a socket connection to the server
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the city and information type to the server
            printWriter.println(operator1);
            printWriter.flush();

            printWriter.println(operator2);
            printWriter.flush();

            printWriter.println(operation_type);
            printWriter.flush();
            String result_of;

            // reads the weather information from the server
            while ((result_of = bufferedReader.readLine()) != null) {
                final String finalizedCurrencyInformation = result_of;

                // updates the UI with the weather information. This is done using postt() method to ensure it is executed on UI thread
                resultView.post(() -> resultView.setText(finalizedCurrencyInformation));
            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
