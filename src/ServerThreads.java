import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerThreads implements Runnable {

    int count;

    List<DatagramSocket> udp_sockets;

    public ServerThreads(List<DatagramSocket> udp_sockets,int count) {
        this.count = count;
        this.udp_sockets = udp_sockets;
    }

    @Override
    public synchronized void run() {
        try {

            int BUFF_SIZE = 256;

            byte[] buf = new byte[BUFF_SIZE];



            DatagramPacket inpacket = new DatagramPacket(buf, buf.length);

            System.out.println("Port " + count + "(" + udp_sockets.get(count).getLocalPort() + ") is listening...");

            while(true) {

                udp_sockets.get(count).receive(inpacket);

                while (Server.status != 0) {

                    System.out.println("Waiting " + udp_sockets.get(count).getLocalPort());

                    Thread.sleep(100);

                }

                Server.status = 1;

                System.out.println("Recieved message from: " + inpacket.getAddress().getHostAddress() + "/" + inpacket.getPort()
                        + " on port " + udp_sockets.get(count).getLocalPort());

                Server.connections++;

                if (Server.log.keySet().contains(inpacket.getAddress().getHostAddress() +
                        "/" + inpacket.getPort())) {

                    System.out.println("Server status: " + Server.status + " " + udp_sockets.get(count).getLocalPort());

                    Server.log.get(inpacket.getAddress().getHostAddress() + "/" + inpacket.getPort()).add(udp_sockets.get(count).getLocalPort());

                    Server.status = 0;

                    System.out.println("Server status: " + Server.status + " " + udp_sockets.get(count).getLocalPort());

                } else {

                    System.out.println("Server status: " + Server.status + " " + udp_sockets.get(count).getLocalPort());


                    Server.log.put(inpacket.getAddress().getHostAddress() +
                            "/" + inpacket.getPort(),new ArrayList<>());
                   Server.log.get(inpacket.getAddress().getHostAddress() +
                           "/" + inpacket.getPort()).add(udp_sockets.get(count).getLocalPort());

                   Server.status = 0;

                    System.out.println("Server status: " + Server.status + " " + udp_sockets.get(count).getLocalPort());

                }
            }

            }catch (Exception ex){ex.printStackTrace();}
    }
}
