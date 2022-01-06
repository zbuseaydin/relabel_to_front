
public class Edge {

    Vertex from;
    Vertex to;
    int capacity;
    int flow;
    Edge residual;

    public Edge(Vertex from, Vertex to, int capacity){
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
    }

    //adds flow to the edge, decreases the flow of the residual edge
    //and updates the excess flow of the vertices that this edge connects
    public void addFlow(int amount){
        this.flow += amount;
        this.residual.flow -= amount;
        this.to.excessFlow += amount;
        this.from.excessFlow -= amount;
    }

    //returns the remaining capacity of the edge
    public int remainingCap(){
        return this.capacity-this.flow;
    }

}

