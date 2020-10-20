import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Connexion {

    Server calledServer;
    LocalDateTime time;

    public Connexion(Server calledServer, LocalDateTime time) {
        this.calledServer = calledServer;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{ calledServer=" + calledServer + ", time=" + time + "}";
    }
}
