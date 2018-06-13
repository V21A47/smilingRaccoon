package DB;

import java.lang.reflect.Field;

public class User{
    private String name;
    private String password;

    private static Db db = null;

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public User(String name, String password){
        db.addUser(name, password);
        this.name = name;
        this.password = password;
    }

    public static boolean userExist(String userName){
        if(db.getUserPassword(userName) != null){
            return true;
        } else {
            return false;
        }
    }

    public boolean changePassword(String newPassword){
        return db.changePassword(name, newPassword);
    }

    public static User getUser(String name){
        String password = db.getUserPassword(name);
        if(password == null){
            return null;
        }

        try{
            User user = new User(null, null);
            Field f = user.getClass().getDeclaredField("name");
            f.setAccessible(true);
            f.set(user, name);
            f = user.getClass().getDeclaredField("password");
            f.setAccessible(true);
            f.set(user, password);

            return user;
        } catch (IllegalAccessException|NoSuchFieldException e){
            System.err.println(e);
        }
        return null;
    }

    public static void setDB(Db newDb){
        db = newDb;
    }
}
