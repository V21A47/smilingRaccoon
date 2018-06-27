package DB;

import java.lang.reflect.Field;

public class User implements AvailableForORM{
    private int id;
    private boolean admin;
    private UserProperties prop;
    private boolean isExpired = false;

    public int getID(){
        return id;
    }

    public void update(){
        this.isExpired = !LittleORM.checkObjectExist(this);
        // from base
    }

    public boolean isAdmin(){
        update();
        return admin;
    }

    public void setName(String name){
        if(isExpired){
            return;
        }
        prop.setName(name);
        LittleORM.updateObject(this);
    }

    public String getName(){
        update();
        return prop.getName();
    }

    public boolean isExpired(){
        update();
        return isExpired;
    }

    public void setPassword(String password){
        prop.setPassword(password);
        LittleORM.updateObject(this);
    }

    public boolean isPassword(String password){
        update();
        if(isExpired){
            return false;
        }
        return prop.isPassword(password);
    }

    public User(){
        ;
    }

    public User(boolean admin, String name, String password){
        prop = new UserProperties(name, password);
        this.admin = admin;

    }
}
