import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class Server
 *
 * @author Saddem
 */
public class Server {

    /**
     * Le nom de serveur.
     */
    String name;

    /**
     * Le nombre des connexions.
     */
    Long nbConnexion = 0L;

    /**
     * La liste des serveurs appelés.
     */
    List<Connexion> listOfCalledServer = new ArrayList<>();

    /**
     * La liste des serveurs appelants.
     */
    List<Connexion> listOfCallingServer = new ArrayList<>();

    /**
     * Constructeur d'un serveur.
     *
     * @param name Le nom de serveur.
     */
    public Server(String name) {
        this.name = name;
    }

    /**
     * Ajouter une liaison avec un serveur appelant.
     *
     * @param server Le serveur qui fait l'appel.
     * @param time   L'heure d'appel.
     */
    public void isCalledBy(Server server, LocalDateTime time) {
        addConnexion(server, time, this.listOfCalledServer);
    }

    /**
     * Ajouter une liaison avec un serveur appelé.
     *
     * @param server Le serveur qui a été appelé.
     * @param time   L'heure d'appel.
     */
    public void isCalling(Server server, LocalDateTime time) {
        addConnexion(server, time, this.listOfCallingServer);
    }

    /**
     * Méthode générique.
     *
     * @param server             Le serveur.
     * @param time               L'heure.
     * @param listOfCallerServer La liste à modifier.
     */
    private void addConnexion(Server server, LocalDateTime time, List<Connexion> listOfCallerServer) {
        if (listOfCallerServer.stream().noneMatch(c -> c.calledServer.name.equals(server.name))) {
            listOfCallerServer.add(new Connexion(server, time));
        }
        this.nbConnexion++;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(name, server.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nbConnexion);
    }
}
