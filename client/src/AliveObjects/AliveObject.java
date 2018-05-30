package AliveObjects;

import Places.SearchableThing;
import java.io.Serializable;

abstract public class AliveObject extends SearchableThing implements Serializable {
    private boolean isAlive = true;
    private int yearOfBirth;

    public AliveObject(){
    }
    public AliveObject(String name, int yearOfBirth, double sizeValue, int x, int y){
        this(name, yearOfBirth, sizeValue);
        this.move(x, y);
    }

    public AliveObject(String name, int yearOfBirth, double sizeValue){
        super(sizeValue, name);
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public boolean move(int x, int y){
        if(isAlive == false){
            return false;
        }

        if(x < -1000 || x > 1000 || y < -1000 || y > 1000){
            return false;
        }

        return super.move(x, y);
    }

    public boolean setYearOfBirth(int yearOfBirth){
        if(!(yearOfBirth <=0) ){
            this.yearOfBirth = yearOfBirth;
            return true;
        } else {
            return false;
        }
    }

    public int getYearOfBirth(){
        return yearOfBirth;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill(){
        isAlive = false;
    }

    @Override
    public int hashCode(){
        int hC = getName().length();
        hC += (int)(this.getSizeValue()*10000 + this.getSizeValue());
        hC += yearOfBirth;
        if(isAlive){
            hC += 10000;
        } else {
            hC += 100000;
        }
        return hC;
    }

    @Override
    public boolean equals(Object object){
        return  object instanceof AliveObject &&
                ((AliveObject) object).isAlive() == isAlive &&
                ((AliveObject) object).getYearOfBirth() == this.yearOfBirth &&
                ((AliveObject) object).getName().equals(getName()) &&
                ((AliveObject) object).getSizeValue() == this.getSizeValue();
    }

    @Override
    public String toString(){
        return getName();
    }
}
