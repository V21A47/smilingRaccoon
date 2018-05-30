package AliveObjects;

import java.io.Serializable;
import Groups.IllegalGroupOperation;
import Places.Searchable;
import java.util.Comparator;

public class Human extends AliveObject implements Speakable, Searchable, Comparable, Serializable {
    private HumanType type = HumanType.NORMAL;
    public ConditionInCommunity condition;
    private String gender = "male";

    public String getGender(){
        return gender;
    }

    public Human(){
        }

    public static class ConditionInCommunity implements Serializable{
        private StateOfFreedom state;
        private int remainingTime;

        public ConditionInCommunity(StateOfFreedom state, int time){
            this.state = state;
            remainingTime = time;
        }

        public StateOfFreedom getState(){
            return state;
        }

        public int getRemainingTime(){
            return remainingTime;
        }

        @Override
        public String toString(){
            if(state == StateOfFreedom.FREE){
                return "free";
            } else if (state == StateOfFreedom.ARRESTED) {
                return "arrested for " + remainingTime + " days";
            } else {
                return "imprisoned for " + remainingTime + " days";
            }
        }
    }

    public static class HumanComparator implements Comparator<Human>{
        @Override
        public int compare(Human a, Human b){
            return(a.compareTo(b));
        }
    }

    public Human(String name, int yearOfBirth, HumanType type, int x, int y){
        super(name, yearOfBirth, 80.0D, x, y);
        this.type = type;
        condition = new ConditionInCommunity(StateOfFreedom.FREE, -1);
    }

    public Human(String name, int yearOfBirth, HumanType type){
        super(name, yearOfBirth, 80.0D);
        this.type = type;
        condition = new ConditionInCommunity(StateOfFreedom.FREE, -1);
    }

    public Human(String name, int yearOfBirth){
        this(name, yearOfBirth, HumanType.NORMAL);
    }

    @Override
    public int compareTo(Object obj){
        if(obj == null){
            return 1;
        }

        Human human = (Human)(obj);
        double result = Math.sqrt((this.getX()*this.getX()) + (this.getY()*this.getY())) -
                        Math.sqrt((human.getX()*human.getX()) + (human.getY()*human.getY()));

        if( result < 0 ){
            return -1;
        } else if ( result == 0){
            return 0;
        }
        return 1;
    }

    @Override
    public void checkObject() throws IllegalGroupOperation {}

    public HumanType getType() {
        return type;
    }

    public ConditionInCommunity getConditionInCommunity(){return condition;}

    public void setState(LawDefender changer, ConditionInCommunity state) {
        this.condition = new ConditionInCommunity(state.getState(), state.getRemainingTime());
    }

    @Override
    public int hashCode(){
        int hC = this.getName().hashCode();
        hC += this.getYearOfBirth();
        switch (type){
            case NORMAL: hC += 1000; break;
            case POLICE: hC += 2000; break;
            case BANDIT: hC += 3000; break;
        }

        if(condition.getState() == StateOfFreedom.FREE){
            hC += 20000;
        } else {
            hC += 50000;
        }

        if(this.isAlive()){
            hC += 10000;
        } else {
            hC += 100000;
        }

        return hC;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Human && ((Human) object).isAlive() == this.isAlive() &&
                ((Human) object).getYearOfBirth() == this.getYearOfBirth() && ((Human) object).getName().equals(this.getName()) &&
                ((Human) object).condition.getState() == condition.getState() && ((Human) object).type == type &&
                ((Human) object).getX() == this.getX() && ((Human) object).getY() == this.getY();
    }

    @Override
    public String toString(){
        return this.type + " " + this.getName();
    }
}
