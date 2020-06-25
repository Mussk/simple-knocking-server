import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class Client extends AbstractUser {

    DatagramSocket socket_udp;

    Socket socket;

    public Client(String[] args) {

        super(args);
    }


    public void runClient(){

        try {

            socket_udp = new DatagramSocket((int) (1024 + (Math.random() * 10000)));

            socket_udp.setSoTimeout(20000);

            int BUFF_SIZE = 256;

            byte[] buf = "Knock!".getBytes();


            for (int i = 0; i < udp_ports.size(); i++) {

              DatagramPacket packet = new DatagramPacket(buf,buf.length,new InetSocketAddress(ip_server,udp_ports.get(i)));

              socket_udp.send(packet);
            }

            System.out.println("Waiting on server reply...");

            byte[] buf1 = new byte[BUFF_SIZE];

            DatagramPacket inpacket = new DatagramPacket(buf1, buf1.length);

            socket_udp.receive(inpacket);

            String data = new String(inpacket.getData(),inpacket.getOffset(),inpacket.getLength());

            System.out.println(data);

            String addr_tcp_server = data.split("/")[0];

           Integer port_tcp_server = Integer.parseInt(data.split("/")[1]);

            System.out.println("Recieved message from: " + addr_tcp_server + "/" + port_tcp_server);

            socket = new Socket();

            socket.connect(new InetSocketAddress(addr_tcp_server,port_tcp_server));

            System.out.println("Connected to server");

            //creating streams
            BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //send msg
            outstream.write("Hello from client!" + "\n");
            outstream.flush();

            //recieve msg
            String recieved_msg = instream.readLine();

            System.out.println("Recieved msg: " + recieved_msg);

            socket.close();

        }catch (Exception ex){ex.printStackTrace();}
    }
}
