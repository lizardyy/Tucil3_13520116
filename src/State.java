import java.util.Comparator;
import java.util.Vector;

class StateComparator implements Comparator<State> {
    @Override
    public int compare(State a, State b) {
        if (a.cost < b.cost)
            return -1;
        if (a.cost > b.cost)
            return 1;
        if(a.cost == b.cost){
            if (a.command.size() <b.command.size()){
                return 1;
            }
            else{
                return -1;
            }
        }
        return 0;
    }
}

class State{
    public State(int Cost, Vector<String> command) {
        this.cost = Cost;
        this.command = new Vector<>(command);
    }
    public int cost;
    public Vector<String> command;
}