import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

   Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client()
    {
        try {

            System.out.println("Sending request to Server");
            socket=new Socket("172.26.144.1",7777);
            System.out.println("connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();

        }catch (Exception e)
        {

            e.printStackTrace();
        }

    }

    public void startReading()
    {
        Runnable r1=()-> {

            System.out.println("Reading Started");

            try {

            while (!socket.isClosed())
            {

                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server terminated the Chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server : "+msg);
                }

            }catch (Exception e) {
                System.out.println("Connection is Closed");;
            }
        };
        new Thread(r1).start();
    }

     public void startWriting()
    {
        Runnable r2=() -> {
            System.out.println("Writer started....");
            try {
            while (!socket.isClosed())
            {

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

                }

            }catch (Exception e)
            {
//               e.printStackTrace();
                System.out.println("Connection Closed..");
               }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client Started");
        new Client();
    }
}
