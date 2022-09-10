package ar.edu.itba.ss;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Locating inputs.txt in resources
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("inputs.txt");
        if (is == null) {
            System.out.println("File not found");
            System.exit(1);
        }

        // Initiate setup
        Scanner scanner = new Scanner(is);
        SimulationHandler simulationHandler = new SimulationHandler();

        // Reads txt
        App.readTxt(simulationHandler, scanner);

        simulationHandler.simInit();
    }

    public static void readTxt(SimulationHandler simulationHandler, Scanner scanner) {
        // Read N value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setN(Integer.parseInt(scanner.next()));
            System.out.println(in + " " + simulationHandler.getN());
        }
        // Read Lx value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setLx(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getLx());
        }
        // Read Ly value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setLy(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getLy());
        }
        // Read RanY value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setRanY(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getRanY());
        }
        // Read rc value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setRc(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getRc());
        }
        // Read pMass value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setPMass(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getPMass());
        }
        // Read pVModule value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setPVModule(Float.parseFloat(scanner.next()));
            System.out.println(in + " " + simulationHandler.getPVModule());
        }
    }

}
