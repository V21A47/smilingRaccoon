package Groups;

import AliveObjects.AliveObject;
import AliveObjects.Human;
import AliveObjects.Speakable;
import Places.Searchable;

public class GroupOfPeople extends  Group implements Speakable, Searchable {
    public GroupOfPeople(String name, int size){
        super(name, size);

    }

    @Override
    public void addMember(AliveObject object){
        if(object instanceof Human){
            super.addMember(object);
        }
    }

    @Override
    public Human getMember(int index){
        return (Human)(super.getMember(index));
    }
}
