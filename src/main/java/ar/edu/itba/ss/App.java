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


        simulate3Maps(simulationHandler);

        // Save data
        System.out.println("Finished");
    }

    private static void simulate3Maps(SimulationHandler simulationHandler) {

            simulationHandler.setN(150);
            JsonPrinter jsonPrinter = new JsonPrinter();


            for (int k = 0; k < 6; k++) {
                simulationHandler.setPVModule(0.01 + k * 0.005);
                DataAccumulator da = new DataAccumulator(simulationHandler.getN());

                for (int j = 0; j < 10; j++) {
                    simulationHandler.simReset();
                    simulationHandler.simInit();
                    simulationHandler.cellIndexMethod();
                    simulationHandler.eventSetup();

                    // External clock setup
                    double lastTime = simulationHandler.getGlobalTime();
                    double step = simulationHandler.getTimeStep();
                    // Start simulation
                    while (!simulationHandler.endCondition()) {
                        if(simulationHandler.iterate()) {
                            double actualTime = simulationHandler.getGlobalTime();
                            if (actualTime - lastTime > step) {
                                lastTime = actualTime;
                                if (j == 0) {
//                                    jsonPrinter.addFpVsT(simulationHandler.getGlobalTime(), simulationHandler.getFp());
                                }
                            }
                        }
                    }
                    // Aca calculamos presion y temperatura y la agregamos al data Accumulator
                    double zeroTime = simulationHandler.getGlobalTime();
                    double actualTime = simulationHandler.getGlobalTime();
                    lastTime = zeroTime;
                    simulationHandler.resetImpulse();
                    while (zeroTime + 10 > actualTime) {
                        actualTime = simulationHandler.getGlobalTime();
                        if (simulationHandler.iterate()) {
                            if (actualTime - lastTime > step) {
                                lastTime = actualTime;
                            }
                        }
                    }
                    double perimeter = simulationHandler.getCompletePerimeter();
                    double PatIteration = simulationHandler.getTotalImpulse() / (perimeter * 10);
                    da.addP(PatIteration);
                }
                jsonPrinter.addPVsT(da.getPressureProm(), simulationHandler.getEnergy(), da.getPressureError());
            }
            String str = String.format("plots/PvsT-%d.json", simulationHandler.getN());
            PrintWriter pwPvsT = openFile(str);
            writeToFile(pwPvsT, jsonPrinter.getpVsTArray().toJSONString());

//            PrintWriter pw = openFile("plots/sim" + simulationHandler.getN() + ".json");
//            writeToFile(pw, jsonPrinter.getFpArray().toJSONString());
    }

    private void iterationPrintingMap(SimulationHandler simulationHandler) {
        PrintWriter pw = null;

        // Particle contour
        int xAmount = (int) (simulationHandler.getLx() / simulationHandler.getPRadius() / 2);
        int yAmount = (int) (simulationHandler.getLy() / simulationHandler.getPRadius() / 2);

        // Get particle size
        int size = simulationHandler.getParticlesList().size() + xAmount * 2 + yAmount * 2 + (yAmount - 5) + 1;
        StringBuilder sb = new StringBuilder(size + "\n\n");
        sb.append(simulationHandler.printParticles());

        // Get walls
        StringBuilder walls = drawWalls(simulationHandler, xAmount, yAmount);

        // External clock setup
        int i = 0;
        int j = 0;
        double lastTime = simulationHandler.getGlobalTime();
        double step = simulationHandler.getTimeStep();

        // Start simulation
        while (!simulationHandler.endCondition()) {
            if(simulationHandler.iterate()) {
                double actualTime = simulationHandler.getGlobalTime();
                if (actualTime - lastTime > step) {
                    lastTime = actualTime;

                    // Obtengo datos
                    StringBuilder s = new StringBuilder(size + "\n\n");
                    s.append(walls);
                    if(i % 1500 == 0) {
                        pw = openFile("output/anim/animation" + j++ + ".xyz");
                    }
                    writeToFile(pw, s.append(simulationHandler.printParticles()).toString());
                    i++;
                }
            }
        }
    }

    private static StringBuilder drawWalls(SimulationHandler simulationHandler, int xAmount, int yAmount) {
        StringBuilder walls = new StringBuilder();
        for (int i = 0; i < xAmount; i++) {
            walls.append(String.format("%f %f 255\n", i * simulationHandler.getPRadius() * 2, 0.0f));
            walls.append(String.format("%f %f 255\n", i * simulationHandler.getPRadius() * 2, simulationHandler.getLy()));
        }
        for (int i = 0; i < yAmount; i++) {
            walls.append(String.format("%f %f 255\n", 0.0f, i * simulationHandler.getPRadius() * 2));
            walls.append(String.format("%f %f 255\n", simulationHandler.getLx(), i * simulationHandler.getPRadius() * 2));
        }
        for (int i = 0; i < (yAmount - 3) / 2; i++) {
            walls.append(String.format("%f %f 255\n", simulationHandler.getLx() / 2, i * simulationHandler.getPRadius() * 2));
            walls.append(String.format("%f %f 255\n", simulationHandler.getLx() / 2, simulationHandler.getLy() - (i * simulationHandler.getPRadius() * 2)));
        }
        return walls;
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
