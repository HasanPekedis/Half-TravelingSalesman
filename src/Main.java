import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.io.PrintWriter;

class Main {

    private static ArrayList<City> cities = new ArrayList<City>();
    private static ArrayList<City> result = new ArrayList<City>();// This is the found optimal path
    private static PrintWriter writer;
    private static long totalLenght = 0;// Lenght of road
    private static int numOfCities = 0;// Number of cities after half
    private static String inputFileName;

    public static void main(String[] args) throws IOException {
        inputFileName = args[0];
        writer = new PrintWriter(new File("output.txt"));

        // To track the runtime
        long startTime = System.nanoTime();

        double means[] = readInput();// Read input also calculates the means of the input

        City startingCity = chooseHalf(means);// ChooseHalf also finds the ideal city to start at

        nearestNeighborSolution(startingCity);

        System.out.println("Length of road: " + totalLenght);

        optimizeResult();

        System.out.println("After Optimization Length of road: " + totalLenght);

        printOutput();

        // To track runtime
        long endTime = System.nanoTime();
        System.out.println("Runtime: " + ((endTime - startTime) / 1000000));// nanoseconds to milliseconds
    }

    // Removes half of cities using statistics.
    private static City chooseHalf(double[] means) {

        double xMean = means[0];
        double yMean = means[1];

        // Rounds number of cities to the nearest integer
        numOfCities = cities.size() / 2;
        if (cities.size() % 2 == 1) {
            ++numOfCities;
        }

        // We now need to calculate the variance for both x and y
        double difX;
        double difY;

        for (int i = 0; i < cities.size(); i++) {
            City current = cities.get(i);
            difX = current.getX() - xMean;

            difY = current.getY() - yMean;

            current.setMeanLength(difX * difX + difY * difY);
        }

        cities.sort(Comparator.comparing(City::getMeanLength));
        for (int i = cities.size() - 1; i >= numOfCities; i--) {
            cities.remove(i);
        }
        return cities.get(0);
    }

    // This is a basic nearest neighbor solution
    private static void nearestNeighborSolution(City startCity) {

        // Determine starting point
        City currentCity = startCity;

        // Set values before loop
        City closestCity = startCity;
        long min = 0;
        long distance = 0;
        ArrayList<City> unvisitedCities = cities;

        // Loop trough each city
        for (int i = 0; i < numOfCities; i++) {

            totalLenght += min;// Add found length
            min = Integer.MAX_VALUE;// This is so min always changes

            currentCity = closestCity;// Go to next city

            result.add(currentCity);
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

    private static void do2Opt(ArrayList<City> result, int i, int j) {
        ArrayList<City> reversed = new ArrayList<>(result.subList(i + 1, j + 1));
        for (int k = i + 1; k <= j; k++) {
            result.set(k, reversed.get(j - k));
        }
    }

    private static void optimizeResult() {

        int n = result.size();

        boolean foundImprovement = true;

        while (foundImprovement) {
            foundImprovement = false;
            for (int i = 0; i <= n - 2; i++) {
                for (int j = i + 1; j <= n - 1; j++) {
                    if (!((i == n - 1) || (j == n - 1))) {
                        long lengthDelta = -getDistance(result.get(i), result.get(i + 1))
                                - getDistance(result.get(j), result.get(j + 1))
                                + getDistance(result.get(i), result.get(j))
                                + getDistance(result.get(i + 1), result.get(j + 1));

                        // If the length of the path is reduced, do a 2-opt swap
                        if (lengthDelta < 0) {
                            do2Opt(result, i, j);
                            totalLenght += lengthDelta;
                            foundImprovement = true;
                        }
                    }
                }
            }
        }

    }

    // Finds the distance between 2 cities.
    private static long getDistance(City city1, City city2) {
        long xDif = city1.getX() - city2.getX();
        long yDif = city1.getY() - city2.getY();
        return (long) Math.round(Math.sqrt(xDif * xDif + yDif * yDif));
    }

    // Reads the input and parses integers
    private static double[] readInput() throws FileNotFoundException {

        Scanner tempScanner = new Scanner(new File(inputFileName));

        // These are for calculating the covariance
        long xSum = 0;
        long ySum = 0;

        // These are used to store the cities
        int id;
        int x;
        int y;
        String currentLine;

        while (tempScanner.hasNextLine()) {

            currentLine = tempScanner.nextLine();
            currentLine = currentLine.trim();

            if (!currentLine.trim().isEmpty()) {

                String[] strings = currentLine.split("\\s+");// Splits from spaces

                id = Integer.parseInt(strings[0]);
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);

                xSum += x;
                ySum += y;

                cities.add(new City(id, x, y));
            }
        }
        tempScanner.close();

        // Calculates means
        double means[] = new double[2];
        means[0] = xSum / cities.size();
        means[1] = ySum / cities.size();

        return means;
    }

    // Simply prints the lenght of the road and cities visited
    private static void printOutput() throws IOException {

        writer.printf(totalLenght + "\n");
        for (City city : result) {
            writer.printf(city.getId() + "\n");
        }
        writer.close();
    }
}