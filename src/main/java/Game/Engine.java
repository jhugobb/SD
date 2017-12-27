package Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Engine {
   private Map<Integer,Set<Integer>> rankSet;
   private Map<Integer,Match> queue;
   private Map<Integer,Player> players;
   private Integer matchN;
   private Integer playerN;
}
