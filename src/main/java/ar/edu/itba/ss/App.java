package ar.edu.itba.ss;

import java.io.*;
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

        PrintWriter pw = openFile("output/anim/animation.xyz");

        // Particle contour
        int xAmount = (int) (simulationHandler.getLx() / simulationHandler.getPRadius() / 2);
        int yAmount = (int) (simulationHandler.getLy() / simulationHandler.getPRadius() / 2);
        int size = simulationHandler.getParticlesList().size() + xAmount * 2 + yAmount * 2;
        StringBuilder sb = new StringBuilder(size + "\n\n");
        StringBuilder walls = new StringBuilder();

        for (int i = 0; i < xAmount; i++) {
            walls.append(String.format("%f %f\n", i * simulationHandler.getPRadius() * 2, 0.0f));
            walls.append(String.format("%f %f\n", i * simulationHandler.getPRadius() * 2, simulationHandler.getLy()));
        }
        for (int i = 0; i < yAmount; i++) {
            walls.append(String.format("%f %f\n", 0.0f, i * simulationHandler.getPRadius() * 2));
            walls.append(String.format("%f %f\n", simulationHandler.getLx(), i * simulationHandler.getPRadius() * 2));
        }
        sb.append(simulationHandler.printParticles());
        writeToFile(pw, sb.append(walls).toString());

        simulationHandler.cellIndexMethod();
        simulationHandler.eventSetup();
        while (!simulationHandler.endCondition()) {
            if(simulationHandler.iterate()) {
                StringBuilder s = new StringBuilder(size + "\n\n");
                s.append(walls);
                writeToFile(pw, s.append(simulationHandler.printParticles()).toString());
            }
        }
        pw.close();
        System.out.println("Finished");
    }


    private static PrintWriter openFile(String filepath) {
        try {
            new File(filepath).delete();
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            return new PrintWriter(bw);
        } catch (Exception e) {
            System.out.println("Failed");
        }
        return null;
    }

    private static void writeToFile(PrintWriter pw, String toWrite) {
        pw.print(toWrite);
        pw.flush();
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
            simulationHandler.setLx(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getLx());
        }
        // Read Ly value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setLy(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getLy());
        }
        // Read RanY value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setRanY(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getRanY());
        }
        // Read rc value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setRc(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getRc());
        }
        // Read pRadius value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setPRadius(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getPRadius());
        }
        // Read pMass value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setPMass(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getPMass());
        }
        // Read pVModule value
        if (scanner.hasNextLine()) {
            String in = scanner.next();
            simulationHandler.setPVModule(Double.parseDouble(scanner.next()));
            System.out.println(in + " " + simulationHandler.getPVModule());
        }
    }

}
