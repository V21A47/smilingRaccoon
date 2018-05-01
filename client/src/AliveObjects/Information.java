package AliveObjects;

public class Information {
    private String data;
    private double importance;
    private AliveObject object;

    public Information(String data, AliveObject object, double importance){
        this.data = data;
        this.object = object;

        if(importance > 1.0D){
            this.importance = 1.0D;
        } else if( importance < 0.0D ){
            this.importance = 0.0D;
        } else {
            this.importance = importance;
        }
    }

    public double getImportance(){
        return importance;
    }

    public String getData(){
        return data;
    }

    public AliveObject getObject() {
        return object;
    }

    @Override
    public int hashCode(){
        return (int)(data.length() + importance*100);
    }

    @Override
    public String toString(){
        return "информация: \"" + data + "\" (важность [0:1] : " + importance + ")";
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Information){
            if(((Information) object).importance == importance &&
                    ((Information) object).data.equals(data) &&
                    ((Information) object).getObject().equals(this.object)){
                return true;
            }
        }
        return false;
    }
}
