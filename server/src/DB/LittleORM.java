package DB;

import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.Driver;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LittleORM{
    private static Connection connection = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            BufferedReader i = new BufferedReader(new FileReader("resources/loginData"));
            String s = i.readLine();
            String login = s.substring(0, s.indexOf(" "));
            String password = s.substring(s.indexOf(" ") + 1);

            connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:9999/studs",
                login, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("LittleORM has just created a connection to the database!");
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            System.out.println();
            byte[] mdBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < mdBytes.length; i++) {
                sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
            return sb.toString().trim();
        } catch (NoSuchAlgorithmException exc) {
            return null;
        }
    }

    private static String getTableName(Class objectClass){
        return "t_" + objectClass.getPackage().getName().toLowerCase() + "_" + objectClass.getSimpleName().toLowerCase();
    }
    private static String getTableName(String className){
        return "t_" + className.replace(".", "_").toLowerCase();
    }

    public static boolean checkObjectExist(AvailableForORM object){
        if( !checkTableExist(object.getClass()) ){
            return false;
        }

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "select count(*) from ( select * from " + getTableName(object.getClass()) + " where id=" + object.getID() + ") as a;" );
            rs.next();
            if(rs.getInt("count") != 0){
                return true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }

    private static boolean checkTableExist(Class objectClass){
        try{

            ResultSet rs = connection.getMetaData()
                .getTables(connection.getCatalog(), null, getTableName(objectClass), null);
            if (!rs.next()) {
                return false;
            }

            //System.out.println(getTableName(objectClass) + " exists");
            rs.close();
        } catch(SQLException e){
            System.out.println("checkTableExist: " + e);
        }
        return true;
    }

    private static boolean checkTableExist(String className){
        try{
            System.out.println(getTableName(className));
            ResultSet rs = connection.getMetaData()
                .getTables(connection.getCatalog(), null, getTableName(className), null);
            if (!rs.next()) {
                return false;
            }

            rs.close();
        } catch(SQLException e){
            System.out.println("checkTableExist: " + e);
        }
        return true;
    }

    private static void createTable(Class objectClass){
        if(checkTableExist(objectClass)){
            //System.out.println("Exist!");
            return;
        }


        String query = "create table " + getTableName(objectClass) + " " +
                       "\n( " +
                       "id integer primary key,\n";

        try{
            for (Field field : objectClass.getDeclaredFields()) {
                field.setAccessible(true);

                String type = field.getGenericType().getTypeName();

                if(type.equals("int") && !field.getName().equals("id")){
                    query = query + field.getName() + " ";
                    query = query + "integer not null,\n";
                } else if (type.equals("double") || type.equals("float")){
                    query = query + field.getName() + " ";
                    query = query + "real not null,\n";
                } else if (type.equals("java.lang.String")){
                    query = query + field.getName() + " ";
                    query = query + "text not null,\n";
                } else if (type.equals("boolean") && !field.getName().equals("isExpired")){
                    query = query + field.getName() + " ";
                    query = query + "boolean not null,\n";
                } else if (type.indexOf(".") > 0){
                    if(!checkTableExist(type)){
                        createTable(Class.forName(type));
                    }
                    query = query + "id_" + field.getName() + " integer references " +
                            "t_" + type.replace(".", "_").toLowerCase() + "(id),\n";

                }

            }
        } catch (Exception e){
            System.err.println("Error was occured while table was being created " + e);
        }

        query = query.substring(0, query.lastIndexOf(","));
        query = query + ");";

        //System.out.println(query);

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
        } catch (SQLException e){
            //System.err.println(e);
        }
    }

    private static int getNextID(Class objectClass){
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery("select count(*) from (select * from " + getTableName(objectClass) + " ) as a;");
            rs.next();
            return rs.getInt("count") + 1;

        } catch (SQLException e){
            System.err.println(e);
        }

        return 1;
    }

    public static AvailableForORM createObject(Class objectClass, ArrayList<Object> list){
        if(objectClass == null){
            System.err.println("ObjectClass is null");
            return null;
        }

        boolean canBeUsed = false;
        for(Class temp : objectClass.getInterfaces()){
            if(temp.getName().equals("DB.AvailableForORM")){
                canBeUsed = true;
            }
        }
        if(!canBeUsed){
            return null;
        }

        if(!checkTableExist(objectClass)){
            createTable(objectClass);
        }

        String query = "insert into " + getTableName(objectClass) + "\n" +
                "values ( " ;

        AvailableForORM object = null;
        try{
            object = (AvailableForORM)objectClass.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            System.err.println(e);
            return null;
        }

        int object_id = getNextID(objectClass);
        try{
            boolean id = false;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String type = field.getGenericType().getTypeName();

                //System.out.println(list.get(0).getClass().getTypeName());
                if(!id){
                    query = query + " " + object_id + ", ";
                    field.set(object, object_id);
                    id = true;
                    continue;
                }

                if(field.getName().equals("isExpired")){
                    continue;
                }

                //System.out.println(field.getName());
                if(type.equals("int") && list.get(0).getClass().getTypeName().equals("java.lang.Integer")){
                    query = query +  " " + (Integer)list.get(0) + ", ";
                    field.set(object, list.get(0));
                } else if ((type.equals("double") || type.equals("float")) && list.get(0).getClass().getTypeName().equals("java.lang.Double")){
                    query = query +  " " + (Double)list.get(0) + ", ";
                    field.set(object, list.get(0));
                } else if (type.equals("java.lang.String") && list.get(0).getClass().getTypeName().equals("java.lang.String")){
                    query = query +  " '" + (String)list.get(0) + "', ";
                    field.set(object, list.get(0));
                } else if (type.equals("boolean") && list.get(0).getClass().getTypeName().equals("java.lang.Boolean")){
                    query = query +  " " + (Boolean)list.get(0) + ", ";
                    field.set(object, list.get(0));
                } else if (type.indexOf(".") > 0){
                    if(!checkTableExist(type)){
                        createTable(Class.forName(type));
                    }
                    AvailableForORM temp = createObject(Class.forName(type), list);

                    query = query + " " + temp.getID() + ", ";
                    field.set(object, temp);
                }
                if(list.size() > 0){
                    list.remove(0);
                }
            }
        } catch (Exception e){
            System.err.println("Error was occured while table was being created " + e);
        }

        query = query.substring(0, query.lastIndexOf(","));
        query = query + ");";

        //System.out.println(query);

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
        } catch (SQLException e){
            //System.err.println(e);
        }

        return object;
    }

    public static boolean removeObject(AvailableForORM object){
        if(object.isExpired() || !checkObjectExist(object)){
            return false;
        }

        try{
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                String type = field.getGenericType().getTypeName();

                if (type.indexOf(".") > 0){
                    removeObject((AvailableForORM)field.get(object));
                }
            }
        } catch (Exception e){
        }

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "delete from " + getTableName(object.getClass()) + " where id=" + object.getID() +";" );
            stmt.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean updateObject(AvailableForORM object){
        if(!checkObjectExist(object)){
            return false;
        }

        String query = "update " + getTableName(object.getClass()) + " set \n";

        try{
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String type = field.getGenericType().getTypeName();

                if(field.getName().equals("isExpired")){
                    continue;
                }

                if(type.equals("int") && !field.getName().equals("id")){
                    query = query +  " " + field.getName() + "=" + (Integer)field.get(object) + ", ";
                } else if ((type.equals("double") || type.equals("float"))){
                    query = query +  " " + field.getName() + "=" + (Double)field.get(object)+ ", ";
                } else if (type.equals("java.lang.String")){
                    query = query +  " " + field.getName() + "='" + (String)field.get(object) + "', ";
                } else if (type.equals("boolean")){
                    query = query +  " " + field.getName() + "=" + (Boolean)field.get(object) + ", ";
                } else if (type.indexOf(".") > 0){
                    query = query + " " + ((AvailableForORM)field.get(object)).getID() + ", ";
                }
            }
        } catch (Exception e){
            System.err.println("Error was occured while table was being created " + e);
            return false;
        }
        query = query.substring(0, query.lastIndexOf(","));
        query = query + " where id=" + object.getID() + ";";

        //System.out.println(query);

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
        } catch (SQLException e){
            //System.err.println(e);
        }

        return true;
    }

    public static AvailableForORM loadObject(Class objectClass, int id){
        if(!checkTableExist(objectClass)){
            return null;
        }
        AvailableForORM object = null;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + getTableName(objectClass) + " where id=" + id + ";");
            try{
                object = (AvailableForORM)objectClass.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                System.err.println(e);
                return null;
            }

            int i = 1;
            while(rs.next()) {
                for(Field field : object.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    String type = field.getGenericType().getTypeName();

                    if(field.getName().equals("isExpired")){
                        field.set(object, false);
                        continue;
                    }

                    if(type.equals("int")){
                        field.set(object, rs.getInt(i));
                        i++;
                    } else if (type.equals("double") || type.equals("float")){
                        field.set(object, rs.getDouble(i));
                        i++;
                    } else if (type.equals("java.lang.String")){
                        field.set(object, rs.getString(i));
                        i++;
                    } else if (type.equals("boolean")){
                        field.set(object, rs.getBoolean(i));
                        i++;
                    } else if (type.indexOf(".") > 0){
                        AvailableForORM temp = loadObject(Class.forName(type), rs.getInt(i));
                        i++;
                        field.set(object, temp);
                    }
                }
            }
        } catch (Exception e){
            System.err.println("Error was occured while table was being created " + e);
        }
        return object;
    }

    public static int getObjectsAmount(Class objectClass){
        if(!checkTableExist(objectClass)){
            return 0;
        }

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "select count(*) from ( select * from " + getTableName(objectClass) + ") as a;" );
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return 0;
    }
   /*
    public static AvailableForORM[] loadObjects(Class objectClass){
        if(!checkTableExist(objectClass)){
            return null;
        }

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + getTableName(objectClass.getClass()) + ";");

            AvailableForORM object = null;
            try{
                object = (AvailableForORM)objectClass.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                System.err.println(e);
                return null;
            }
            int i = 1;
            while(rs.next()) {
                for(Field field : object.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    String type = field.getGenericType().getTypeName();

                    if(field.getName().equals("isExpired")){
                        field.set(object, false);
                        continue;
                    }

                    if(type.equals("int")){
                        field.set(object, rs.getInt(i));
                        i++;
                    } else if (type.equals("double") || type.equals("float")){
                        field.set(object, rs.getDouble(i));
                        i++;
                    } else if (type.equals("java.lang.String")){
                        field.set(object, rs.getString(i));
                        i++;
                    } else if (type.equals("boolean")){
                        field.set(object, rs.getBoolean(i));
                        i++;
                    } else if (type.indexOf(".") > 0){
                        AvailableForORM temp = loadObject(Class.forName(type), rs.getInt(i));
                        i++;
                        field.set(object, temp);
                    }
                }
            }
        } catch (Exception e){
            System.err.println("Error was occured while table was being created " + e);
        }

        return null;
    }*/
}
