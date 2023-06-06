import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.PrintWriter;

class Main {

    private static ArrayList<City> cities = new ArrayList<City>();
    private static ArrayList<City> result = new ArrayList<City>();// This is the found optimal path
    private static ArrayList<City> temp = new ArrayList<City>();
    private static PrintWriter writer;
    private static int totalLenght = 0;// Lenght of road
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

        //regionSolution(means);

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
        for(int i = cities.size()-1;i >= numOfCities;i--) {
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
        int min = 0;
        int distance = 0;
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

    private static void regionSolution(double[] means){
        double xMean = means[0];
        double yMean = means[1];

        double r = Math.sqrt(cities.get(cities.size()-1).getMeanLength());

        double t =  r / numOfCities;

        t *= 485;

        System.out.print(t);
        if(t <= 0){
            t = Double.MIN_VALUE;
        }


        // Put all cities to proper region
        City tp;
        double dif;
        long size_Cities = cities.size();
        int loop = (int) (r / t);
        loop += 2;
        int j;
        boolean ascend = true;


        // 1. Region traverse
        for(int i = 0; i < loop; i++) {
            j = 0;
            while (j < size_Cities) {
                tp = cities.get(j);
                if (tp.getY() - yMean >= 0) {
                    dif = tp.getX() - xMean;
                    if (dif >= t*i && dif <= t*(i+1)) {
                        temp.add(tp);
                        size_Cities--;
                        cities.remove(j);
                        continue;
                    }
                }
                j++;
            }
            if (temp.size() > 0) {
                if (ascend) {
                    temp.sort(Comparator.comparing(City::getY));
                } else {
                    temp.sort(Comparator.comparing(City::getY).reversed());
                }
                ascend = !ascend;
                tempToResult();
            }
        }
        ascend = false;


        // 4. Region
        for(int i = loop; i > 0; i--) {
            j = 0;
            while (j < size_Cities) {
                tp = cities.get(j);
                if (tp.getY() - yMean < 0) {
                    dif = tp.getX() - xMean;
                    if (dif >= (i - 1) * t && dif <= t*i ) {
                        temp.add(tp);
                        size_Cities--;
                        cities.remove(j);
                        continue;
                    }
                }
                j++;
            }
            if (temp.size() > 0) {
                if (ascend) {
                    temp.sort(Comparator.comparing(City::getY));
                } else {
                    temp.sort(Comparator.comparing(City::getY).reversed());
                }
                ascend = !ascend;
                tempToResult();
            }
        }

        // 3. Region
        for(int i = 0; i < loop; i++) {
            j = 0;
            while (j < size_Cities) {
                tp = cities.get(j);
                if (tp.getY() - yMean < 0) {
                    dif = xMean - tp.getX();
                    if (dif >= i*t && dif <= t*(i+1)) {
                        temp.add(tp);
                        size_Cities--;
                        cities.remove(j);
                        continue;
                    }
                }
                j++;
            }
            if (temp.size() > 0) {
                if (ascend) {
                    temp.sort(Comparator.comparing(City::getY));
                } else {
                    temp.sort(Comparator.comparing(City::getY).reversed());
                }
                ascend = !ascend;
                tempToResult();
            }
        }

        ascend = true;
        // 2. Region
        for(int i = loop; i > 0; i--) {
            j = 0;
            while (j < size_Cities) {
                tp = cities.get(j);
                if (tp.getY() - yMean >= 0) {
                    dif = xMean - tp.getX();
                    if (dif >= (i - 1) * t && dif <= t*i ){
                        temp.add(tp);
                        size_Cities--;
                        cities.remove(j);
                        continue;
                    }
                }
                j++;
            }
            if (temp.size() > 0) {
                if (ascend) {
                    temp.sort(Comparator.comparing(City::getY));
                } else {
                    temp.sort(Comparator.comparing(City::getY).reversed());
                }
                ascend = !ascend;
                tempToResult();
            }
        }


        for(int i = 0; i < result.size() - 1; i++){
            totalLenght += getDistance(result.get(i), result.get(i+1));
        }
        totalLenght += getDistance(result.get(0), result.get(result.size()-1));



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

        // result.get(i).dist2(result.get(j))
        while (foundImprovement) {
            foundImprovement = false;
            for (int i = 0; i <= n - 2; i++) {
                for (int j = i + 1; j <= n - 1; j++) {
                    if (!((i == n - 1) || (j == n - 1))) {
                        int lengthDelta = -getDistance(result.get(i), result.get(i + 1))
                                - getDistance(result.get(j), result.get(j + 1))
                                + getDistance(result.get(i), result.get(j)) + getDistance(result.get(i + 1), result.get(j + 1));

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

    private static void printPath(ArrayList<City> cities) { //If you want to see diffrence between optimezed result and non-optimized result, you can use print path.
        for (City city : cities) {
            System.out.print("--> "  + city.getId());
        }
        System.out.println();
    }

    // Finds the distance between 2 cities.
    private static int getDistance(City city1, City city2) {
        int xDif = city1.getX() - city2.getX();
        int yDif = city1.getY() - city2.getY();
        return (int) Math.round(Math.sqrt(xDif * xDif + yDif * yDif));
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

    private static void tempToResult(){
        for (int i = 0; i < temp.size(); i++){
            result.add(temp.get(i));
        }
        temp.clear();
    }
}