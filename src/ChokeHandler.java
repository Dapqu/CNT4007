public class ChokeHandler {

    public int preferredNeighbors;
    public int UnchokingInterval;

    ChokeHandler(){
        preferredNeighbors = ConfigService.ConfigurationFileVariables.numberOfPreferredNeighbors;
        UnchokingInterval = ConfigService.ConfigurationFileVariables.unchokingInterval;
    }
    public void run(){

    }
}
