import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class Client extends JFrame {

   Socket socket;

    BufferedReader br;
    PrintWriter out;

    private  JLabel heading=new JLabel("Client Area");
    private JTextArea massageArea= new JTextArea();
    private JTextField massageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public Client()
    {
        try {

            System.out.println("Sending request to Server");
            socket=new Socket("172.26.144.1",7777);
            System.out.println("connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
         //   startWriting();

        }catch (Exception e)
        {

            e.printStackTrace();
        }

    }

    private void handleEvents()
    {
        massageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode()==10)
                {
                 String contantToSend=massageInput.getText();
                 massageArea.append("Me : "+contantToSend+"\n");
                 out.println(contantToSend);
                 out.flush();
                 massageInput.setText("");
                 massageInput.requestFocus();
                }

            }
        });

    }

    private void createGUI()
    {
        this.setTitle("Client Messanger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for component
        heading.setFont(font);
        massageArea.setFont(font);
        massageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        massageArea.setEditable(false);

        //Setting Frame Layout
        this.setLayout(new BorderLayout());

        //Adding the component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(massageArea);
        jScrollPane.setAutoscrolls(true);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(massageInput,BorderLayout.SOUTH);

        this.setVisible(true);

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
                        JOptionPane.showMessageDialog(this,"Server terminated the Chat");
                        massageInput.disable();
                        socket.close();
                        break;
                    }

                    //System.out.println("Server : "+msg);
                massageArea.append("Server : "+msg+"\n");
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
