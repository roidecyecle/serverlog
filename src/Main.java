import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        final String filePath = args.length >= 1 ? args[0] : null;
        if (filePath != null && !filePath.isBlank()) {
            File file = new File(filePath);
            if (file.exists()) {
                final String interval = args.length >= 2 ? args[1] : null;
                ServerFileLog fileLog = new ServerFileLog(file,
                        (interval != null && interval.matches("[0-9]]")) ? Integer.parseInt(interval) : 1);

                final String requestServer = args.length >= 3 ? args[2] : null;
                if(requestServer != null && !requestServer.isBlank()) {
                    System.out.println("Les serveurs appelés par ("+ requestServer +") sont "
                            +fileLog.getCalledServerBy(requestServer));
                    System.out.println("Les serveurs qui appelent ("+ requestServer +") sont "
                            +fileLog.getCallingServersFor(requestServer));
                }

                List<Server> servers = fileLog.getMostConnectedServer();
                if (!servers.isEmpty()) {
                    System.out.println("Le/Les serveur(s) qui ont réalisé plus de connexions (nb cnx: " + servers.get(0).nbConnexion + ")");
                    System.out.println(servers);
                }
            } else {
                System.out.println("Le fichier n'existe pas !!");
            }
        } else {
            System.out.println("Vérifier les paramètres d'entrés ! ");
            System.out.println("le premier paramètre doit etre le chemin du fichier ");
            System.out.println("le deuxième paramètre doit etre l'intervalle de temps ");
            System.out.println("le troisième paramètre doit etre le serveur en question");
        }
    }
}
