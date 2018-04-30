package AliveObjects;

import Groups.IllegalGroupOperation;
import Places.Searchable;

public class Human extends AliveObject implements Speakable, Searchable, Comparable {
    private String birthCertificateNumber = "";
    private HumanType type;
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

    public Human(String name, int age, HumanType type){
        super(name, age, 80.0D);

        for(int i = 0; i < 16; i++){
            char [] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            birthCertificateNumber = String.format("%s%s", birthCertificateNumber, alphabet[(int)(Math.random() * 16)]);
        }

        this.type = type;
        condition = new ConditionInCommunity(1.0D, StateOfFreedom.FREE, -1);
    }

    public Human(String name, int age){
        this(name, age, HumanType.NORMAL);
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    @Override
    public int compareTo(Object obj){
        if(obj == null){
            return 1;
        }

        Human human = (Human)(obj);
        for(int i = 0; i < human.getBirthCertificateNumber().length(); i++){
            if(birthCertificateNumber.charAt(i) < human.getBirthCertificateNumber().charAt(i)){
                return -1;
            } else if(i == human.getBirthCertificateNumber().length() &&
                    birthCertificateNumber.charAt(i) == human.getBirthCertificateNumber().charAt(i)){
                return 0;
            }
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
        this.condition = new ConditionInCommunity(state.getPublicAcceptance(), state.getState(), state.getRemainingTime());
    }

    @Override
    public int hashCode(){
        int hC = this.getName().hashCode();
        hC += this.getAge();
        hC += this.birthCertificateNumber.hashCode();
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
