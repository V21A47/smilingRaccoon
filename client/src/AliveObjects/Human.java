package AliveObjects;

import Groups.IllegalGroupOperation;
import Places.Searchable;

public class Human extends AliveObject implements Speakable, Searchable, Comparable {
    private HumanType type;
    private int yearOfBirth;
    public ConditionInCommunity condition;

    public static class ConditionInCommunity{
        private double publicAcceptance;
        private StateOfFreedom state;
        private int remainingTime;

        public ConditionInCommunity(double acceptance, StateOfFreedom state, int time){
            publicAcceptance = acceptance;
            this.state = state;
            remainingTime = time;
        }

        public StateOfFreedom getState(){
            return state;
        }

        public int getRemainingTime(){
            return remainingTime;
        }

        public double getPublicAcceptance(){
            return publicAcceptance;
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

    public Human(String name, int age, HumanType type, int x, int y){
        super(name, age, 80.0D, x, y);
        this.yearOfBirth = 2018 - age;
        this.type = type;
        condition = new ConditionInCommunity(1.0D, StateOfFreedom.FREE, -1);
    }
    
    public Human(String name, int age, HumanType type){
        super(name, age, 80.0D);
        this.yearOfBirth = 2018 - age;
        this.type = type;
        condition = new ConditionInCommunity(1.0D, StateOfFreedom.FREE, -1);
    }

    public Human(String name, int age){
        this(name, age, HumanType.NORMAL);
        this.yearOfBirth = 2018 - age;
    }

    @Override
    public int compareTo(Object obj){
        if(obj == null){
            return 1;
        }

        Human human = (Human)(obj);
        double result = Math.pow((this.getX()*this.getX()) + (this.getY()*this.getY()), 1/2) -
                        Math.pow((human.getX()*human.getX()) + (human.getY()*human.getY()), 1/2);
            
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

    public int getYearOfBirth(){
        return yearOfBirth;
    }
        
    public ConditionInCommunity getConditionInCommunity(){return condition;}

    public void setState(LawDefender changer, ConditionInCommunity state) {
        this.condition = new ConditionInCommunity(state.getPublicAcceptance(), state.getState(), state.getRemainingTime());
    }

    @Override
    public int hashCode(){
        int hC = this.getName().hashCode();
        hC += this.getAge();
        hC += this.yearOfBirth;
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
                ((Human) object).getAge() == this.getAge() && ((Human) object).getName().equals(this.getName()) &&
                ((Human) object).condition.getState() == condition.getState() && ((Human) object).type == type;
    }

    @Override
    public String toString(){
        return this.type + " " + this.getName();
    }
}
