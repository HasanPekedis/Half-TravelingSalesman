public class City {

    private int id;
    private int x;
    private int y;

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

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getId(){
        return this.id;
    }

    public String toString(){
        return "Id: " + id + "x: " + x + "y: " + y;
    }

}