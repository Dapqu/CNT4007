import java.util.HashSet;
import java.util.Random;

public class OptimisticUnchokeHandler implements Runnable{
    //every x seconds, loop over peerlist and get everyone with weChokedThem true
    //Choose among them who to unchoke

    public void run() {
        HashSet<Peer> chokedlist = new HashSet<>(ConfigService.getchoked());
        int size = chokedlist.size();
        int item = new Random().nextInt(size);
        int i = 0;
        Peer peerChosen;
        for(Peer obj : chokedlist)
        {
            if (i == item)
                peerChosen = obj;
            i++;
        }

        //peerChosen send unchoke

    }


}
