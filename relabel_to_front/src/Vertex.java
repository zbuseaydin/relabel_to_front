import java.util.Vector;

public class Vertex {

    String id;
    int height=0;
    Vector<Edge> edges = new Vector<>();
    int excessFlow=0;

    public Vertex(String id){
        this.id = id;
    }

    public void addEdge(Vertex to, int capacity){
        Edge e1 = new Edge(this, to, capacity);
        Edge e2 = new Edge(to, this, 0);
        this.edges.add(e1);
        to.edges.add(e2);
        e1.residual = e2;
        e2.residual = e1;
    }

    public boolean isTransport(){
        if(id.contains("gt"))
            return true;
        if(id.contains("gr"))
            return true;
        if(id.contains("rr"))
            return true;
        if(id.contains("rt"))
            return true;
        return false;
    }

    //creates edges to the vertices in the given transport vector
    public void createBagEdges(Vector<Vertex> transport, int capacity){
        for(Vertex ver: transport)
            addEdge(ver,capacity);
    }
    
    //pushes flow to the adjacent vertices, if the remaining capacity of the edge is greater than 0
    //and the height of the adjacent vertex is smaller
    //if push can not be performed, returns false
    public boolean push(){
        for(Edge e: edges){
            if(e.remainingCap()<=0)
                continue;
            if(e.to.height<height){
                e.addFlow(Math.min(excessFlow, e.remainingCap()));
                return true;
            }
        }
        return false;
    }
}
