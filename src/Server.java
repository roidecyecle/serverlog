import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {

    String name;
    Long nbConnexion = 0l;
    List<Connexion> listOfCalledServer = new ArrayList<>();
    List<Connexion> listOfCallingServer = new ArrayList<>();

    public Server(String name) {
        this.name = name;
    }

    public void isCalledBy(Server server, LocalDateTime time){
        addConnexion(server, time, this.listOfCalledServer);
    }

    public void isCalling(Server server, LocalDateTime time){
        addConnexion(server, time, this.listOfCallingServer);
    }

    private void addConnexion(Server server, LocalDateTime time, List<Connexion> listOfCallerServer) {
        if (!listOfCallerServer.stream().anyMatch(c -> c.calledServer.name == server.name)) {
            listOfCallerServer.add(new Connexion(server, time));
        }
        this.nbConnexion++;
    }

    @Override
    public String toString() {
        return name ;
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
