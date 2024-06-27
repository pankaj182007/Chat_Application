import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server()  {
        try {
            //to accept connection
            server=new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting....");
            socket=server.accept();

            //to read and writing 
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
        //Thread to reading data
        Runnable r1=()->{
            System.out.println("Reader started....");
            while (true)
            {
                String msg;
                try {
                     msg=br.readLine();

                if (msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");

                    break;
                }
                System.out.println("Client : "+msg);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        
        new Thread(r1).start();
    }

    public void startWriting()
    {
        //thread to writing data
        Runnable r2=()->{
            System.out.println("Writer started....");
            while (true)
            {
                try {
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }


        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("Server Started.....");
        new Server();
    }
}
