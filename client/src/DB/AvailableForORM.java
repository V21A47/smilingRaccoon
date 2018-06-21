package DB;

public interface AvailableForORM{
    public int getID();
    public boolean isExpired();
    public void update();
}
