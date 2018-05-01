package AliveObjects;

public enum StateOfFreedom {
    //TODO: a problem with serializable enum states
    FREE("FREE"),
    ARRESTED("ARRESTED"),
    IMPRISONED("IMPRISONED");

    private String state;
    StateOfFreedom(String name){
        this.state = name;
    }

    @Override
    public String toString(){
        return this.state;
    }

}
