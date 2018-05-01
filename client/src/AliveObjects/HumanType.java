package AliveObjects;

public enum HumanType {

    NORMAL("обычный житель"),
    POLICE("полицейский"),
    BANDIT("бандит");

    private String humanType;

    HumanType(String presentation){
        this.humanType = presentation;
    }

    @Override
    public String toString(){
        return humanType;
    }
}