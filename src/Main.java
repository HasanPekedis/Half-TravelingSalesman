import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;

class Main {

    private static ArrayList<City> cities = new ArrayList<City>();
    private static ArrayList<City> results = new ArrayList<City>();// This is the found optimal path
    private static PrintWriter writer;
    private static int totalLenght = 0;// Lenght of road
    private static int numOfCities = 0;// Number of cities after half
    private static String inputFileName;

    public static void main(String[] args) throws IOException {
        inputFileName = args[0];
        writer = new PrintWriter(new File("output.txt"));

        // Starts by reading the inputs
        readInput();

        // Chooses half of the cities
        chooseHalf();

        // This is an example of a nearest neigbor solution
        nearestNeighborSolution();

        // Prints output
        printOutput();
    }

    // Removes half of the cities. Will improve upon this
    private static void chooseHalf() {

        numOfCities = cities.size() / 2;
        if (cities.size() % 2 == 1) {
            ++numOfCities;
        }

        for (int i = cities.size(); i > numOfCities; i--) {
            cities.remove(i - 1);
        }

    }

    // This is a basic nearest neighbor solution
    private static void nearestNeighborSolution() {

        // Determine starting point
        City currentCity = cities.get(0);
        City startCity = currentCity;

        // Set values before loop
        City closestCity = cities.get(0);
        int min = 0;
        int distance = 0;
        ArrayList<City> unvisitedCities = cities;

        // Loop trough each city
        for (int i = 0; i < numOfCities; i++) {

            totalLenght += min;// Add found length
            min = Integer.MAX_VALUE;// This is so min always changes

            currentCity = closestCity;// Go to next city

            results.add(currentCity);
            unvisitedCities.remove(currentCity);

            // Find nearest neighbor
            for (int j = 0; j < unvisitedCities.size(); j++) {
                distance = getDistance(currentCity, unvisitedCities.get(j));
                if (distance < min) {
                    closestCity = unvisitedCities.get(j);
                    min = distance;
                }
            }
        }

        // Finally, go back to the city you started from
        totalLenght += getDistance(startCity, closestCity);

    }

    // Finds the distance between 2 cities.
    private static int getDistance(City city1, City city2) {
        int xDif = city1.getX() - city2.getX();
        int yDif = city1.getY() - city2.getY();
        return (int) Math.round(Math.sqrt(xDif * xDif + yDif * yDif));
    }

    // Reads the input and parses integers
    private static void readInput() throws FileNotFoundException {

        Scanner tempScanner = new Scanner(new File(inputFileName));

        int id;
        int x;
        int y;
        String currentLine;

        while (tempScanner.hasNextLine()) {

            currentLine = tempScanner.nextLine();
            currentLine = currentLine.trim();

            if (!currentLine.trim().isEmpty()) {

                String[] strings = currentLine.split("\\s+");//Splits from spaces
                
                id = Integer.parseInt(strings[0]);
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);

                cities.add(new City(id, x, y));
            }
        }
        tempScanner.close();
    }

    //Simply prints the lenght of the road and cities visited
    private static void printOutput() throws IOException {

        writer.printf(totalLenght + "\n");
        for (City city : results) {
            writer.printf(city.getId() + "\n");
        }
        writer.close();
    }
}