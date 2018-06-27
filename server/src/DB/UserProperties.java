package DB;

public class UserProperties implements AvailableForORM{
    private int id;
    private String name;
    private String password;
    private boolean isExpired = false;

    public UserProperties(){
        ;
    }

    public UserProperties(String name, String password){
        this.name = name;
        this.password = password;
    }

    public void update(){
        this.isExpired = !LittleORM.checkObjectExist(this);
        // from base
    }

    public void setPassword(String password){
        this.password = password;
        LittleORM.updateObject(this);
    }

    public boolean isExpired(){
        update();
        return isExpired;
    }

    public int getID(){
        return id;
    }

    public void setName(String name){
        this.name = name;
        LittleORM.updateObject(this);
    }

    public String getName(){
        update();
        return name;
    }

    public boolean isPassword(String password){
        update();
        return this.password.equals(password);
    }

}
