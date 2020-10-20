import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        ServerFileLog fileLog = new ServerFileLog(new File("log.txt"));

        //System.out.println(fileLog.getCalledServerBy("meriem"));

        List<Server> servers = fileLog.getMostConnectedServer();
        if(!servers.isEmpty()) {
            System.out.println("Le/Les serveur(s) qui ont réalisé plus de connexions (nb cnx: " + servers.get(0).nbConnexion + ")");
            System.out.println(servers);
        }
    }
}
