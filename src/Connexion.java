import java.time.LocalDateTime;

/**
 * Class Connexion: englobe un serveur appelé
 *
 * @author Saddem
 */
public class Connexion {

    /**
     * Le serveur appelé.
     */
    Server calledServer;

    /**
     * L'heure d'appel.
     */
    LocalDateTime time;

    /**
     * Constrructeur d'une connexion.
     *
     * @param calledServer Le serveur appelé.
     * @param time         L'heure d'appel.
     */
    public Connexion(Server calledServer, LocalDateTime time) {
        this.calledServer = calledServer;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{ calledServer=" + calledServer + ", time=" + time + "}";
    }
}
