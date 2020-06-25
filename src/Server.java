
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends AbstractUser implements Runnable {

    static volatile Map<String,List<Integer>> log = new HashMap<>();

    List<DatagramSocket> udp_sockets = new ArrayList<>();

    static volatile int status; //0 free, 1 busy

    static volatile  int connections = 0;

    public Server(String[] args) {

        super(args);
    }

    @Override
    public void run() {

        Integer tmp_connections = 0;

        while (true) {

            if (connections != tmp_connections) {

                tmp_connections = connections;

                if (connections % udp_sockets.size() == 0) {

                    while (status != 0) {

                        try {

                            Thread.sleep(100);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    status = 1;

                    System.out.println("Log: status 1");

                    //    System.out.println(log.entrySet());

                    String record_client = "";

                    for (Map.Entry<String, List<Integer>> entry :
                            log.entrySet()) {

                        if (entry.getValue().size() == udp_sockets.size()) {

                            int count = 0;

                            for (int i = 0; i < udp_sockets.size(); i++) {

                                if (entry.getValue().contains(udp_sockets.get(i).getLocalPort())) {

                                    count++;

                                    if (count == 3) record_client = entry.getKey();
                                }
                            }

                            if (count == udp_sockets.size()) {

                                //        System.out.println("OK, send TCP credits");

                                Thread th1 = new Thread(new TcpServer(ip_server, record_client.split("/")[0],
                                        Integer.parseInt(record_client.split("/")[1])));

                                th1.start();

                                try {
                                    th1.join();

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }

                    status = 0;
                    System.out.println("Log status 0");
                }
            }
        }
    }



    public void runServer() {

        System.out.println("Server: " + ip_server);

        try {

            for (int i = 0; i < udp_ports.size(); i++) {



                udp_sockets.add(new DatagramSocket(udp_ports.get(i)));

                new Thread(new ServerThreads(udp_sockets,i)).start();

            }

            while (connections < 1){}

            new Thread(this).start();


        }catch (Exception ex){ ex.printStackTrace();}

    }
}
