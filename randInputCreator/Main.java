package randInputCreator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("input.txt");
        Random rand = new Random();

        
        for (int i = 0; i < 50_000; i++ ){
            printWriter.print(i + " ");//id
            printWriter.print(rand.nextInt(20_000) + " ");
            printWriter.print(rand.nextInt(20_000) + "\n");
        }
        printWriter.close();
    }
}
