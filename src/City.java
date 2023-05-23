public class City {

    private int id;
    private int x;
    private int y;
    private double zScoreX;
    private double zScoreY;


    public City(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setZScoreX(double z){
        this.zScoreX = z;
    }

    public void setZScoreY(double z){
        this.zScoreY = z;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getId(){
        return this.id;
    }

    public double getZScoreX(){
        return this.zScoreX;
    }

    public double getZScoreY(){
        return this.zScoreY;
    }

    public String toString(){
        return "Id: " + id + "x: " + x + "y: " + y;
    }

}