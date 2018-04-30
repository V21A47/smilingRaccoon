package Groups;

import AliveObjects.AliveObject;

public class Group {
    private String name;
    private int size;
    private int membersAmount;
    private AliveObject[] members;

    public Group(String name, int size){
        this.name = name;
        this.size = size;
        membersAmount = 0;
        members = new AliveObject[size];
    }

    public void checkObject() throws IllegalGroupOperation{
        if(membersAmount == 0){
            throw new IllegalGroupOperation("Группа \"" + getName() + "\" не может выполнять действия, она ведь пуста.");
        }
    }

    public String getName() {
        return name;
    }

    public int getMembersAmount() {
        return membersAmount;
    }

    public int getSize() {
        return size;
    }

    public AliveObject getMember(int index){
        if(index < 0 || index >= membersAmount){
            return null;
        } else {
            return members[index];
        }
    }

    public void addMember(AliveObject object){
        if(membersAmount == size){
            return;
        }
        membersAmount += 1;
        members[membersAmount-1] = object;
    }

    @Override
    public int hashCode(){
        int hC = name.hashCode();
        hC += size + membersAmount;
        for(int i = 0; i < membersAmount; i++){
            hC += members[i].getName().length();
        }
        return hC;
    }

    @Override
    public String toString(){
        return "группа \"" + name + "\"";
    }

    @Override
    public boolean equals(Object object){ if(object instanceof Group) { try{ if(!((Group) object).getName().equals(name)){ return false; } } catch (NullPointerException npe){ if(name != null || ((Group)object).getName() != null){ return false; } }
            if(((Group) object).getMembersAmount() == membersAmount &&
                    ((Group) object).getSize() == size){
                for(int i = 0; i < membersAmount; i++){
                    if(!((Group) object).getMember(i).equals(members[i]) ){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
