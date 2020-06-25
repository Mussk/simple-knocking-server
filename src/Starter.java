import java.io.IOException;

/**
 * @author
 *  Oleksandr Karpenko, s16934, 20c
 * **/


public class Starter {

    public static void main(String[] args) {

        try {
            if (args[0].equals("server"))
                runServer(args); //server <ports udp>

            else if
            (args[0].equals("client")) runClient(args); // client ip_server <ports udp>

        } catch (Exception ex) {
        }


    }

    public static void runServer(String[] args) throws IOException {

        new Server(args).runServer();
    }


    public static void runClient(String[] args) throws IOException {

         new Client(args).runClient();

    }


}
