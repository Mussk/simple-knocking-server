import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class TcpServer implements Runnable {

    String ip_server, ip_client;
    Integer port_server, port_client;


    public TcpServer(String ip_server,String ip_client, Integer port_client) {

        this.port_server = (int) (1024 + (Math.random() * 10000));
        this.ip_server = ip_server;
        this.ip_client = ip_client;
        this.port_client =  port_client;
    }

    @Override
    public void run() {

        try {
            int BUFF_SIZE = 256;

            byte[] buf = new byte[BUFF_SIZE];

            buf = (ip_server + "/" + port_server.toString()).getBytes();

            DatagramSocket udp_socket = new DatagramSocket((int) (1024 + (Math.random() * 10000)));

            ServerSocket s_socket = new ServerSocket(port_server);

               System.out.println("TCP starts listening...");

            //send TCP info
            udp_socket.send(new DatagramPacket(buf,buf.length,new InetSocketAddress(ip_client,port_client)));

            Socket socket = s_socket.accept();

            //create streams
            BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String recieved_msg = instream.readLine();

            System.out.println("Recieved msg: " + recieved_msg);

            //send answer
            outstream.write("Hello from server!" + "\n");
            outstream.flush();

            socket.close();





        }catch (Exception ex){ex.printStackTrace();}
    }


}
