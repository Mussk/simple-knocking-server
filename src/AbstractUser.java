import java.beans.IntrospectionException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUser {


    List<Integer> udp_ports = new ArrayList<>(); //client and server

    Integer tcp_port; //server and client

    String  ip_server; //server

    String ip_client;  //client


    public AbstractUser(String[] args) {

        if (args[0].equals("server")) {//server

            try {
                for (int i = 1; i < args.length; i++) {

                    this.udp_ports.add(Integer.parseInt(args[i])); //kolejnosc knocking
                }

               deleteCopies();

                this.tcp_port = (int) (1024 + (Math.random() * 10000));
                this.ip_server = InetAddress.getLocalHost().getHostAddress();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        else if (args[0].equals("client")) { //client


            try {
                for (int i = 2; i < args.length; i++) {

                    this.udp_ports.add(Integer.parseInt(args[i])); //kolejnosc knocking
                }
                this.ip_server = args[1];
                this.ip_client = InetAddress.getLocalHost().getHostAddress();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deleteCopies(){

        for (int i = 0; i < udp_ports.size(); i++) {

            for (int j = udp_ports.size() - 1; j > i ; j--) {

                if (udp_ports.get(j).equals(udp_ports.get(i))){

                    udp_ports.remove(j);
                }


            }
        }
    }
}
