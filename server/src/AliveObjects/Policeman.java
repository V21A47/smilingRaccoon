package AliveObjects;

public class Policeman extends Human implements LawDefender {
    private Information groundsForActivity;

    public Policeman(String name){
        super(name, (int)(Math.random()*45+20), HumanType.POLICE);
    }

    public boolean getGrounds(AliveObject object){
        return true;
    }

    public void arrest(Human human){
        if(getGrounds(human)){
            Human.ConditionInCommunity condition = new ConditionInCommunity(0.7, StateOfFreedom.ARRESTED, 14);
            human.condition = condition;
        }
    }

    public void free(Human human){
        if(getGrounds(human)){
            Human.ConditionInCommunity condition = new ConditionInCommunity(1.0D, StateOfFreedom.FREE, -1);
            human.condition = condition;
        }
    }

    public void imprison(Human human, int days){
        Human.ConditionInCommunity condition = new ConditionInCommunity(0.5, StateOfFreedom.IMPRISONED, days);
        human.condition = condition;
    }

    public void receive(Information data){
        this.groundsForActivity = data;
        this.say("Понял, будут предприняты все возможные меры.");
    }
}
