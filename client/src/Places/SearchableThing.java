package Places;
import java.io.Serializable;

public abstract class SearchableThing implements Serializable{
    private double sizeValue;
    private String name;
    private int x;
    private int y;

    public static int maxX = 350;
    public static int minX = -350;
    public static int maxY = 300;
    public static int minY = -300;

    public SearchableThing(){

    }

    public SearchableThing(double sizeValue){
        if(sizeValue < 0.0D){
            this.sizeValue = 0.0D;
        } else {
            this.sizeValue = sizeValue;
        }
    }

    public SearchableThing(double sizeValue, String name, int x, int y){
        this(sizeValue, name);
        if(! move(x, y)){
            move(SearchableThing.minX, SearchableThing.minY);
        }
    }

    public SearchableThing(double sizeValue, String name){
        if(sizeValue < 0.0D){
            this.sizeValue = 0.0D;
        } else {
            this.sizeValue = sizeValue;
        }
        this.name = name;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean move(int x, int y){
        if(x < SearchableThing.minX || x > SearchableThing.maxX){
            return false;
        }

        if(y < SearchableThing.minY || y > SearchableThing.maxY){
            return false;
        }

        this.x = x;
        this.y = y;
        return true;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getSizeValue(){
        return sizeValue;
    }

    public void setSizeValue(double sizeValue){
        this.sizeValue = sizeValue;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof SearchableThing){
            if(((SearchableThing) object).getSizeValue() == this.sizeValue){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (int)(sizeValue*10000 + sizeValue);
    }

    @Override
    public String toString(){
        return "Объект, который можно найти, с параметром размера = " + sizeValue;
    }
}
