package AliveObjects;

import Groups.IllegalGroupOperation;

public interface LawDefender{
    default void arrest(Human human){
        checkObject();
        if(human.condition.getState() != StateOfFreedom.IMPRISONED) {
            Human.ConditionInCommunity cond = new Human.ConditionInCommunity(StateOfFreedom.ARRESTED, 14);
            human.setState(this, cond);
        }
    }

    default void free(Human human){
        checkObject();
        if(human.condition.getState() != StateOfFreedom.FREE){
            Human.ConditionInCommunity cond = new Human.ConditionInCommunity(StateOfFreedom.FREE, -1);
            human.setState(this, cond);
        }
    }

    default void imprison(Human human, int days){
        checkObject();
        Human.ConditionInCommunity cond = new Human.ConditionInCommunity(StateOfFreedom.IMPRISONED, days);
        human.setState(this, cond);
    }

    void checkObject() throws IllegalGroupOperation;
}
