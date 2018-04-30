package Groups;

import AliveObjects.AliveObject;
import AliveObjects.LawDefender;
import AliveObjects.Policeman;

public class GroupOfPolicemen extends GroupOfPeople implements LawDefender {

    public GroupOfPolicemen(String name, int size){
        super(name, size);
    }

    @Override
    public void addMember(AliveObject object){
        if(object instanceof Policeman) {
            super.addMember(object);
        }
    }

    @Override
    public Policeman getMember(int index){
        return (Policeman)(super.getMember(index));
    }
}
