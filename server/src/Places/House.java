package Places;

public class House extends Building {
    private String address;

    public class Yard extends Building {
        private Building wall;
        private String address;

        Yard(House house){
            super("двор", 6000.0D);
            wall = new Building("стена, принадлежащая \"" + this + "\",", 2000.0D);
            address = house.getAddress();
        }

        public Building getWall() {
            return wall;
        }
    }

    public Yard yard;

    public House(String name, String address){
        super(name, 5000.0D);
        this.address = address;
        yard = new Yard(this){
            {
                setName("двор \"" + name + "\" по адресу \"" + address + "\"");
            }
        };
    }

    public String getAddress() {
        return address;
    }
}
