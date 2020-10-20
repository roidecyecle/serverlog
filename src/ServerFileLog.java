import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerFileLog {

    public static final String DELIMITER_SPACE = " ";
    
    public Map<String, Server> listServer = new HashMap<>();

   public ServerFileLog(File file) throws IOException {
       BufferedReader br = new BufferedReader(new FileReader(file));
       br.lines().map(st -> st.split(DELIMITER_SPACE))
               .filter(line -> getDateFromLong(line[0]).isAfter(LocalDateTime.now().minusHours(1)))
               .forEach(line -> this.addConnexion(line[1], line[2], getDateFromLong(line[0])));
    }

    private LocalDateTime getDateFromLong(String string) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(string)), TimeZone.getDefault().toZoneId());
    }

    private boolean isExist(String name){
        return listServer.containsKey(name);
    }

    public List<Server> getCallingServersFor(String serverName){
       List<Server> result = new ArrayList<>();
       if(isExist(serverName)) {
           result = listServer.get(serverName).listOfCalledServer.stream()
                   .map(x -> listServer.get(x.calledServer.name))
                   .collect(Collectors.toList());
       }
       return result;
    }

    public List<Server> getCalledServerBy(String serverName){
        List<Server> result = new ArrayList<>();
        if(isExist(serverName)) {
            result = listServer.get(serverName).listOfCallingServer.stream()
                .map(x->listServer.get(x.calledServer.name))
                .collect(Collectors.toList());
        }
        return result;
    }

    public List<Server> getMostConnectedServer(){
      Long max = listServer.entrySet().stream()
              .max((o1, o2) -> o1.getValue().nbConnexion.compareTo(o2.getValue().nbConnexion))
              .get().getValue().nbConnexion;

      return listServer.entrySet().stream()
              .filter(entry -> entry.getValue().nbConnexion.equals(max))
              .map(entry -> entry.getValue())
              .collect(Collectors.toList());
    }

    private Server addServer(String name){
        Server server = null;
        if(!isExist(name)){
            server = new Server(name);
            listServer.put(name, server);
        }else{
            server = listServer.get(name);
        }
        return server;
    }

    private void addConnexion(String caller, String called, LocalDateTime time){
        Server callerServer = addServer(caller);
        Server calledServer = addServer(called);
        calledServer.isCalledBy(callerServer, time);
        callerServer.isCalling(calledServer, time);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        listServer.forEach((key, value) -> {
            Server server = (Server) value;
            stringBuilder.append("Serveur " + server.name + " - Nombre total de connexion: " + "(" + server.nbConnexion + ")\n");
            stringBuilder.append("la liste des serveurs qui appelent " + server.name + "\n");
            stringBuilder.append(server.listOfCalledServer.toString() + "\n");
            stringBuilder.append("la liste des serveurs appelés par " + server.name + "\n");
            stringBuilder.append(server.listOfCallingServer.toString() + "\n");
            stringBuilder.append("------------------------------------------------------\n");
        });
        return stringBuilder.toString();
    }
}
