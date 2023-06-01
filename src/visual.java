import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//1800 800
public class CityVisualization extends JFrame {
    private ArrayList<City> cities;
    private ArrayList<Integer> output;
    

    public CityVisualization(ArrayList<City> cities, ArrayList<Integer> output) {
        this.cities = cities;
        this.output = output;
        setTitle("City Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void paint(Graphics g) {

        

        super.paint(g);
        for (City city : cities) {
            
            double x = city.getX(); // Şehir X koordinatı
            double y = city.getY(); // Şehir Y koordinatı
            g.fillOval((int) Math.round(x), (int) Math.round(y), 10, 10); // Şehri temsil etmek için küçük bir daire
                                                                          // çizdiriyoruz

        }
        g.setColor(Color.RED);
        ((Graphics2D) g).setStroke(new BasicStroke(2));

        for (int i = 0; i < output.size() - 1; i++) {

            City city1 = cities.get(output.get(i));
            City city2 = cities.get(output.get(i + 1));

            double x1 = city1.getX();
            double y1 = city1.getY();
            double x2 = city2.getX();
            double y2 = city2.getY();

            g.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
        }

        City city1 = cities.get(output.get(output.size() - 1));
        City city2 = cities.get(output.get(0));

        double x1 = city1.getX();
        double y1 = city1.getY();
        double x2 = city2.getX();
        double y2 = city2.getY();

        g.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));


        g.setColor(Color.BLUE);
        City city11 = cities.get(output.get(0));

        double x11 = city11.getX();
        double y11 = city11.getY();
        
        g.fillOval((int) Math.round(x11), (int) Math.round(y11), 10, 10);

    }

    public static double getMaxX(ArrayList<City> cityList) {
        double maxX = 0;
        for (City city : cityList) {
            if (city.getX() > maxX) {
                maxX = city.getX();
            }
        }
        return maxX;
    }

    public static double getMaxY(ArrayList<City> cityList) {
        double maxY = 0;
        for (City city : cityList) {
            if (city.getY() > maxY) {
                maxY = city.getY();
            }
        }
        return maxY;
    }

    public static void multiplyXCoordinates(ArrayList<City> cityList, double multiplier) {
        for (City city : cityList) {
            city.setX(city.getX() * multiplier);
        }
    }

    public static void multiplyYCoordinates(ArrayList<City> cityList, double multiplier) {
        for (City city : cityList) {
            city.setY(city.getY() * multiplier);
        }
    }

    public static ArrayList<Integer> readIntArrayFromFile(String filePath) {
        ArrayList<Integer> numberList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    int number = Integer.parseInt(line);
                    numberList.add(number);
                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz sayı formatı: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numberList;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Metin dosyasının yolu ve ikinci argümanı giriniz.");
            return;
        }
        ArrayList<City> cities = readCitiesFromFile(args[0]);
        ArrayList<Integer> output = readIntArrayFromFile(args[1]);

        double maxX = getMaxX(cities);
        double maxY = getMaxY(cities);

        System.out.println(maxX);
        System.out.println(maxY);

      



        multiplyXCoordinates(cities, 1500 / maxX);
        multiplyYCoordinates(cities, 800 / maxY);

        System.out.println(cities.get(0).getX());
        System.out.println(cities.get(0).getY());

        SwingUtilities.invokeLater(() -> {
            CityVisualization cityVisualization = new CityVisualization(cities, output);
            cityVisualization.setVisible(true);
        });

    }

    private static ArrayList<City> readCitiesFromFile(String filePath) {
        ArrayList<City> cities = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Geçersiz format: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    cities.add(new City(id, x, y));

                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz sayı formatı: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

}

class City {
    private int id;
    private double x;
    private double y;

    public City(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}