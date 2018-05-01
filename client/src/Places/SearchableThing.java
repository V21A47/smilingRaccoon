package Places;

public abstract class SearchableThing{
    private double sizeValue;
    private String name;
    private int x;
    private int y;
    
    public SearchableThing(double sizeValue){
        if(sizeValue < 0.0D){
            this.sizeValue = 0.0D;
        } else {
            this.sizeValue = sizeValue;
        }
    }

    public SearchableThing(double sizeValue, String name, int x, int y){
        this(sizeValue, name);
        this.x = x;
        this.y = y;
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
    
    public void move(int x, int y){
        this.x = x;
        this.y = y;
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
