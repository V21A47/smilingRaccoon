package DB;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.Driver;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;

public class Db{
    private Connection c = null;

    public Db(){
        try {
            Class.forName("org.postgresql.Driver");
            BufferedReader i = new BufferedReader(new FileReader("resources/loginData"));
            String s = i.readLine();
            String login = s.substring(0, s.indexOf(" "));
            String password = s.substring(s.indexOf(" ") + 1);

            c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/lab8_db",
                login, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public boolean changePassword(String userName, String newPassword){
        try{
            if(getUserPassword(userName) == null){
                return false;
            }
            Statement stmt = c.createStatement();

            stmt.executeUpdate("update user_data set password='" + newPassword + "' where user_name='" + userName + "';");
            stmt.close();
            return true;
        } catch (SQLException e){
            System.err.println(this + " " + e);
        }
        return false;
    }

    public boolean addUser(String userName, String password, boolean admin){
        checkUserTableExist();


        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from (select * from user_data where user_name='" + userName + "') as a;");

            rs.next();
            if(rs.getInt("count") != 0){
                return false;
            }

            stmt.executeUpdate("insert into user_data (user_name, password, admin) values ('" + userName + "', '" + password + "', " + admin + ");");
            stmt.close();
        } catch (SQLException e){
            System.err.println(this + " " + e);
        }
        return true;
    }

    public String getUserPassword(String userName){

        if(!checkUserTableExist()){
            return null;
        }

        String result = null;

        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "select password from user_data where user_name='" + userName + "';" );
            while ( rs.next() ) {
                result = rs.getString("password");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e){
            System.err.println(this + " " + e);
        }
        return result;
    }

    public boolean getUserA(String userName){

        if(!checkUserTableExist()){
            return false;
        }

        boolean result = false;

        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "select admin from user_data where user_name='" + userName + "';" );
            while ( rs.next() ) {
                result = rs.getBoolean(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e){
            System.err.println(this + " " + e);
        }
        return result;
    }

    private boolean checkUserTableExist(){
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "select count(*) from (select * from pg_tables where tablename='user_data') as a;" );
            rs.next();
            int amount = rs.getInt("count");
            rs.close();
            if(amount == 0){
                stmt.close();
                stmt = c.createStatement();
                stmt.executeQuery( "create table user_data" +
                                   "(id serial primary key, "+
                                   " user_name text not null," +
                                   " password text not null, " +
                                   " admin boolean not null);");
                stmt.close();
                return false;
            } else {
                return true;
            }
        } catch (SQLException e){
            return false;
        }
    }
}
