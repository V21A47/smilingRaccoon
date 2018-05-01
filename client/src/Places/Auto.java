package Places;

import AliveObjects.Speakable;

public class Auto extends Place {
    private Speakable owner;
    public Place wheels[];

    public Auto(String name, Speakable owner){
        super(name, 3000.0D);
        this.owner = owner;

        wheels = new Place[4];
        for(int i = 0; i < 4; i++){
            wheels[i] = new Place("шина колеса № " + (i+1) + ", принадлежащая " + getName(), 15.0D);
        }
    }

    @Override
    public void setName(String name){
        setName(name);
        for(int i = 0; i < 4; i++){
            wheels[i].setName("Шина колеса №" + (i+1) + ", принадлежащая " + name);
        }
    }

    public void setOwner(Speakable owner){
        this.owner = owner;
    }

    @Override
    public void Hide(){
        super.Hide();
        for(int i = 0; i < 4; i++){
            wheels[i].Hide();
        }
    }

    @Override
    public void Show(){
        super.Show();
        for(int i = 0; i < 4; i++){
            wheels[i].Show();
        }
    }

    public Place getWheel(int index) {
        if(index < 0 || index >= 4){
            return null;
        } else {
            return wheels[index];
        }
    }
}
