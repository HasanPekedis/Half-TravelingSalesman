import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;


class Main{

    private static ArrayList<City> cities = new ArrayList<City>();
    private static ArrayList<City> results = new ArrayList<City>();
    private static PrintWriter writer;
    private static int totalLenght = 9;

    public static void main(String[] args) throws IOException {

        writer = new PrintWriter(new File("output.txt"));
        
        readInput();

        for (City city: cities){
            results.add(city);
        }

        printOutput();
    }

    private static void readInput() throws FileNotFoundException{
        Scanner tempScanner = new Scanner(new File("input.txt"));
        
        int id;
        int x;
        int y;
        while (tempScanner.hasNextLine()){
            String[] strings = tempScanner.nextLine().split(" ");
            id = Integer.parseInt(strings[0]);
            x = Integer.parseInt(strings[1]);
            y = Integer.parseInt(strings[2]);
            cities.add(new City(id,x,y));

        }
        tempScanner.close();
    }

    
    private static void printOutput() throws IOException{
        
        writer.printf(totalLenght + "\n");
        for (City city: results){
            writer.printf(city.getId() + "\n");
        }
        writer.close();
    }
}