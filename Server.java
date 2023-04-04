import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Axel Wale-James
 */

public class Server {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public String Evaluation(String clientChoice, String serverChoice){
        String result = "";
        String weakness = "";

        switch (clientChoice){
            case "rock": 
                weakness = "paper";
                break;
            case "paper":
                weakness = "scissors";
                break;
            case "scissors":
                weakness = "rock";
                break;
        }

        if (serverChoice.equals(weakness)){
            result = "Client: " + clientChoice + " Server: " + serverChoice + " Winner: Server!";
        } else {
            if (serverChoice.equals(clientChoice)){
                result = "Client: " + clientChoice + " Server: " + serverChoice + " Winner: Tie!";
            } else {
                result = "Client: " + clientChoice + " Server: " + serverChoice + " Winner: Client!";
            }
        }

        return result;
    }

    public Server (int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
            System.out.println("Waiting for client...");
            
            //Yields entire process until a client connects
            socket = serverSocket.accept();
            System.out.println("Client accepted!");

            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            outputStream = new DataOutputStream(socket.getOutputStream());

            //Loop to read message from the client
            String message = "";
            while (!message.equals("Quit")){
                try {
                    message = inputStream.readUTF();
                    
                    String choice = "";

                    switch (new Random().nextInt(3)){
                        case 0: 
                            choice = "rock";
                            break;
                        case 1: 
                            choice = "paper";
                            break;
                        case 2: 
                            choice = "scissors";
                            break;
                    }

                    outputStream.writeUTF(Evaluation(message, choice));
                    outputStream.flush();

                } catch (IOException io){
                    System.out.println(io);
                }
            }

            //Closing the connection
            socket.close();
            inputStream.close();
            outputStream.close();

        } catch (IOException io){
            System.out.println(io);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a port number: ");

        if (scanner.hasNextInt()){
            int chosenPort = scanner.nextInt();
            new Server(chosenPort);
        } else {
            System.out.println("Enter an actual number.\n");
        }
        scanner.close();
    }
}

