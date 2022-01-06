import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class Network {
    int maxFlow = 0;
    int total = 0;
    int heightCount = 0;
    int numTransport = 0;

    Vertex s = new Vertex("source");
    Vertex t = new Vertex("sink");
    Vector<Vertex> redTrains = new Vector<>();
    Vector<Vertex> greenTrains = new Vector<>();
    Vector<Vertex> redReindeer = new Vector<>();
    Vector<Vertex> greenReindeer = new Vector<>();

    //linked list that contains all the vertices of the network except source and sink
    LinkedList<Vertex> relabelFront = new LinkedList<>();

    //contains the capacities of the 8 types (b, c, d, e, bd, be, cd, ce) of bag vertices
    int[] bagCapacities = new int[8];

    public Network(){
        Arrays.fill(bagCapacities, 0);
    }
    
    //creates the transport(train and reindeer) vertices
    public void addTransport(String id, Vector<Vertex> transport, int capacity){
        Vertex v = new Vertex(id);
        relabelFront.add(v);
        v.addEdge(t, capacity);
        transport.add(v);
        v.height = 1;
        numTransport++;
    }

    public void updateBagCapacities(String id, int capacity){
        switch (id) {
            case "b" -> bagCapacities[0] += capacity;
            case "c" -> bagCapacities[1] += capacity;
            case "d" -> bagCapacities[2] += capacity;
            case "e" -> bagCapacities[3] += capacity;
            case "bd" -> bagCapacities[4] += capacity;
            case "be" -> bagCapacities[5] += capacity;
            case "cd" -> bagCapacities[6] += capacity;
            case "ce" -> bagCapacities[7] += capacity;
        }
    }

    public void createBags(){
        addBag("b", bagCapacities[0]);
        addBag("c", bagCapacities[1]);
        addBag("d", bagCapacities[2]);
        addBag("e", bagCapacities[3]);
        addBag("bd", bagCapacities[4]);
        addBag("be", bagCapacities[5]);
        addBag("cd", bagCapacities[6]);
        addBag("ce", bagCapacities[7]);
    }

    //creates the bag vertices and their edges
    public void addBag(String id, int capacity){
        if(capacity==0)
            return;
        Vertex v = new Vertex(id);
        relabelFront.add(v);
        v.height = 2;
        s.addEdge(v, capacity);
        total += capacity;

        if(id.contains("a"))
            capacity = 1;

        if(id.contains("bd")){
            v.createBagEdges(greenTrains, capacity);

        }else if(id.contains("be")){
            v.createBagEdges(greenReindeer,capacity);

        }else if(id.contains("cd")){
            v.createBagEdges(redTrains, capacity);

        }else if(id.contains("ce")){
            v.createBagEdges(redReindeer, capacity);

        }else if(id.contains("b")){
            v.createBagEdges(greenReindeer, capacity);
            v.createBagEdges(greenTrains, capacity);

        }else if(id.contains("c")){
            v.createBagEdges(redReindeer, capacity);
            v.createBagEdges(redTrains, capacity);

        }else if(id.contains("d")){
            v.createBagEdges(redTrains, capacity);
            v.createBagEdges(greenTrains, capacity);

        }else if(id.contains("e")){
            v.createBagEdges(redReindeer, capacity);
            v.createBagEdges(greenReindeer, capacity);

        }else{
            v.createBagEdges(greenReindeer, capacity);
            v.createBagEdges(greenTrains, capacity);
            v.createBagEdges(redReindeer, capacity);
            v.createBagEdges(redTrains, capacity);
        }
    }

    //initializes the network for the relabel-to-front algorithm
    //sets the height of the source as the size of the network
    //pushes flow from source to its adjacent vertices
    public void preFlow(){
        s.height = relabelFront.size()+2;

        for(Edge e: s.edges){
            if(e.remainingCap()>0)
                e.addFlow(e.capacity);
        }
    }

    //updates the height of the vertex v as the (minimum height among its accessible neighbors) + 1
    public void relabel(Vertex v){
        int minHeight = Integer.MAX_VALUE;
        for(Edge e: v.edges){
            if(e.remainingCap()<=0)
                continue;
            if(e.to.height<minHeight){
                minHeight = e.to.height;
            }
            if(v.isTransport()&&v.height==1)
                heightCount++;
            v.height = minHeight+1;
        }
    }

    //picks the vertex that has excess flow
    public Vertex nextVertex(){
        Iterator<Vertex> itr = relabelFront.iterator();
        Vertex v;
        while(itr.hasNext()){
            v = itr.next();
            if(v.excessFlow>0)
                return v;
        }return null;
    }

    //while there exists a vertex with excess flow and there exists transport vertex whose height didn't change
    //if can't push, relabels and puts that vertex, if not a transport, in the front of the list
    //maximum flow is the excess flow of the sink
    public void relabelToFront(){
        preFlow();
        while(true){
            Vertex ver = nextVertex();
            if(heightCount>=numTransport || ver == null )
                break;
            if(!ver.push()){
                relabel(ver);
                relabelFront.remove(ver);
                if(ver.isTransport())
                    relabelFront.add(ver);
                else
                    relabelFront.addFirst(ver);
            }
        }
        maxFlow = t.excessFlow;
    }

    public int minRemaining(){
        return total-maxFlow;
    }

}
