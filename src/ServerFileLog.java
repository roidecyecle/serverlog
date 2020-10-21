import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class ServerFileLog: est la classe qui represente un fichier log
 *
 * @author Saddem
 */
public class ServerFileLog {

    /**
     * Le séparateur du fichier
     */
    public static final String DELIMITER_SPACE = " ";

    /**
     * Map de la liste des serveurs.
     */
    public Map<String, Server> listServer = new HashMap<>();

    /**
     * Constructeur de la classe.
     *
     * @param file     Le fichier de log.
     * @param interval L'intervalle des connexions à considérer.
     * @throws IOException En cas une exception se lève.
     */
    public ServerFileLog(File file, int interval) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.lines().map(st -> st.split(DELIMITER_SPACE))
                .filter(line -> getDateFromLong(line[0]).isAfter(LocalDateTime.now().minusHours(interval)))
                .forEach(line -> this.addConnexion(line[1], line[2], getDateFromLong(line[0])));
    }

    /**
     * Transforme un timesTamp à une date.
     *
     * @param string Le timestamp.
     * @return La date convertie.
     */
    private LocalDateTime getDateFromLong(String string) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(string)), TimeZone.getDefault().toZoneId());
    }

    /**
     * Vérifie si l'instance d'un serveur est existe ou pas.
     *
     * @param name Le serveur à vérifier.
     * @return true s'il existe ou false sinon.
     */
    private boolean isExist(String name) {
        return listServer.containsKey(name);
    }

    /**
     * Chercher les serveurs qui appellent un serveur donné.
     *
     * @param serverName Le serveur en question.
     * @return La liste des serveurs.
     */
    public List<Server> getCallingServersFor(String serverName) {
        List<Server> result = new ArrayList<>();
        if (isExist(serverName)) {
            result = listServer.get(serverName).listOfCalledServer.stream()
                    .map(x -> listServer.get(x.calledServer.name))
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Chercher les serveurs appellés par serveur donné.
     *
     * @param serverName Le serveur en question.
     * @return La liste des serveurs.
     */
    public List<Server> getCalledServerBy(String serverName) {
        List<Server> result = new ArrayList<>();
        if (isExist(serverName)) {
            result = listServer.get(serverName).listOfCallingServer.stream()
                    .map(x -> listServer.get(x.calledServer.name))
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Chercher le serveur qui a fait le plus des connexions.
     *
     * @return La liste des serveurs
     */
    public List<Server> getMostConnectedServer() {
        Long max = !listServer.isEmpty()
                ? listServer.entrySet().stream()
                .max(Comparator.comparing(o -> o.getValue().nbConnexion))
                .get().getValue().nbConnexion
                : null;

        return max != null ? listServer.values().stream()
                .filter(server -> server.nbConnexion.equals(max))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    /**
     * Crééer ou ajouter un serveur à la liste des serveurs.
     *
     * @param name Le serveur à ajouter ou vérifier.
     * @return L'instance du serveur.
     */
    private Server addServer(String name) {
        Server server;
        if (!isExist(name)) {
            server = new Server(name);
            listServer.put(name, server);
        } else {
            server = listServer.get(name);
        }
        return server;
    }

    /**
     * Ajouter un connexion entre deux serveurs.
     *
     * @param caller Le serveur appelant.
     * @param called Le serveur appelé.
     * @param time   L'heure d'appel.
     */
    private void addConnexion(String caller, String called, LocalDateTime time) {
        Server callerServer = addServer(caller);
        Server calledServer = addServer(called);
        calledServer.isCalledBy(callerServer, time);
        callerServer.isCalling(calledServer, time);
    }

    /**
     * Synthèse des serveurs.
     *
     * @return une chaine récapitulative de tous les serveurs.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        listServer.forEach((key, value) -> {
            Server server = (Server) value;
            stringBuilder.append("Serveur ").append(server.name).append(" - Nombre total de connexion: ")
                    .append("(").append(server.nbConnexion).append(")\n").append("la liste des serveurs qui appelent ")
                    .append(server.name).append("\n").append(server.listOfCalledServer.toString()).append("\n")
                    .append("la liste des serveurs appelés par ").append(server.name).append("\n")
                    .append(server.listOfCallingServer.toString()).append("\n\n");
        });
        return stringBuilder.toString();
    }
}
