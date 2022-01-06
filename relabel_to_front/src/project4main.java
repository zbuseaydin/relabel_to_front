import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class project4main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(args[0]));
        PrintStream out = new PrintStream(new FileOutputStream(args[1]));

        Network network = new Network();

        int numGreenTrains = Integer.parseInt(in.readLine());
        String[] capGreenTrains;
        if(numGreenTrains!=0){
            capGreenTrains = in.readLine().split(" ");
            for(int i=0; i<numGreenTrains; i++) {
                if(capGreenTrains[i].equals("0"))
                    continue;
                network.addTransport("gt"+i, network.greenTrains, Integer.parseInt(capGreenTrains[i]));
            }
        }else
            in.readLine();

        int numRedTrains = Integer.parseInt(in.readLine());
        String[] capRedTrains;
        if(numRedTrains!=0){
            capRedTrains = in.readLine().split(" ");
            for(int i=0; i<numRedTrains; i++) {
                if(capRedTrains[i].equals("0"))
                    continue;
                network.addTransport("rt"+i, network.redTrains, Integer.parseInt(capRedTrains[i]));
            }
        }else
            in.readLine();

        int numGreenDeer = Integer.parseInt(in.readLine());
        String[] capGreenDeer;
        if(numGreenDeer!=0){
            capGreenDeer = in.readLine().split(" ");
            for(int i=0; i<numGreenDeer; i++) {
                if(capGreenDeer[i].equals("0"))
                    continue;
                network.addTransport("gr"+i, network.greenReindeer, Integer.parseInt(capGreenDeer[i]));
            }
        }else
            in.readLine();

        int numRedDeer = Integer.parseInt(in.readLine());
        String[] capRedDeer;
        if(numRedDeer!=0){
            capRedDeer = in.readLine().split(" ");
            for(int i=0; i<numRedDeer; i++){
                if(capRedDeer[i].equals("0"))
                    continue;
                network.addTransport("rr"+i, network.redReindeer, Integer.parseInt(capRedDeer[i]));
            }
        }else
            in.readLine();

        int numBags = Integer.parseInt(in.readLine());
        if(numBags!=0) {
            String[] bags = in.readLine().split(" ");

            for (int i = 0; i < numBags * 2 - 1; i += 2) {
                if (bags[i + 1].equals("0"))
                    continue;

                if (bags[i].contains("a"))
                    network.addBag(bags[i] + (i / 2), Integer.parseInt(bags[i + 1]));

                else
                    network.updateBagCapacities(bags[i], Integer.parseInt(bags[i + 1]));
            }
            network.createBags();

        }else
            in.readLine();
        
        in.close();

        network.relabelToFront();
        out.println(network.minRemaining());
        
        out.close();
	}

}
