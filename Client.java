import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author Axel Wale-James
 */

public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private DataInputStream serverStream;

    public Client(String address, int port){
        try {
            //Connect to port and sets up data streams
            socket = new Socket(address,port);
            System.out.println("Connected to port:"  + port);

            inputStream = new DataInputStream(System.in);

            outputStream = new DataOutputStream(socket.getOutputStream());

            serverStream = new DataInputStream(socket.getInputStream());

        } catch (UnknownHostException unknown){
            System.out.println(unknown);
            return;

        } catch (IOException io){
            System.out.println(io);
            return;
        }

        //Loop to handle user input and sent it to the server
        String message = "";
        while (!message.equals("Quit")){
            try {
                System.out.println("Your move? (rock, paper or scissors)");
                message = inputStream.readLine();

                if (message.equals("rock") || message.equals("paper") || message.equals("scissors")){
                    outputStream.writeUTF(message);

                    //Server's response to your move
                    String serverMessage = serverStream.readUTF();
                    System.out.println(serverMessage);
                } else if (!message.equals("Quit")) {
                    System.out.println("Wrong input. Try again.\n");
                }

            } catch (IOException io){
                System.out.println(io);
            }
        }

        //Closing the connection 
        try {
            inputStream.close();
            outputStream.close();
            serverStream.close();
            socket.close();
        } catch (IOException io){
            System.out.println(io);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String address; 
        int port; 

        System.out.print("Enter an ip address: ");
        if (scanner.hasNext()){
            address = scanner.next();
        } else {
            System.out.println("Enter an actual address.\n");
            scanner.close();
            return;
        }

        System.out.print("Enter a port number: ");
        if (scanner.hasNextInt()){
            port = scanner.nextInt();
        } else {
            System.out.println("Enter an actual number.\n");
            scanner.close();
            return;
        }

        new Client(address, port);
        scanner.close();
    }
}