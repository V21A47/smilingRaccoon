package AliveObjects;

import Places.SearchableThing;

abstract public class AliveObject extends SearchableThing {
    private boolean isAlive = true;
    private int age;

    public AliveObject(String name, int age, double sizeValue){
        super(sizeValue, name);
        this.age = age;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        if(!(age <=0) ){
            this.age = age;
        } else {
            System.out.println("!");
            this.age=42;
        }
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
        hC += age;
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
                ((AliveObject) object).getAge() == this.age &&
                ((AliveObject) object).getName().equals(getName()) &&
                ((AliveObject) object).getSizeValue() == this.getSizeValue();
    }

    @Override
    public String toString(){
        return getName();
    }
}
