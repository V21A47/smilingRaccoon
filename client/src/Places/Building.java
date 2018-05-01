package Places;

public class Building extends Place {
    private boolean isRuined;

    Building(String name, double sizeValue){
        super(name, sizeValue);
        isRuined = false;
    }

    public boolean isRuined() {
        return isRuined;
    }

    public void Crush(){
        System.out.println(this + " разрушено.");
        isRuined = true;
    }
}
