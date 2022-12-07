import java.util.HashSet;

public class OptimisticUnchokeHandler {
    //every x seconds, loop over peerlist and get everyone with weChokedThem true
    //Choose among them who to unchoke

    HashSet<Peer> chokedlist = new HashSet<>(ConfigService.getchoked());

}
