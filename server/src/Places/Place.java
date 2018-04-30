package Places;

public class Place extends SearchableThing {
    private int objectsAmount = 0;
    private int maxObjectsAmount = 5;
    private SearchableThing objects[] = new SearchableThing[maxObjectsAmount];
    private double sumOfSizes = 0.0D;
    private boolean isHidden = false;

    public Place(String name){
        super(0.0D, name);
    }

    public Place(String name, double sizeValue){
        super(sizeValue, name);
    }

    public void addObject(SearchableThing object) throws AllocationInPlaceException{
        if(objectsAmount == maxObjectsAmount || (sumOfSizes + object.getSizeValue()) >= getSizeValue()){
            throw new AllocationInPlaceException("Невозможно добавить в \"" + getName() + "\" объект с именем \"" +
                    object.getName() + "\", уже нет свободного места.");
        } else {
            objectsAmount += 1;
            sumOfSizes += object.getSizeValue();
            objects[objectsAmount-1] = object;
        }
    }

    public void removeObject(String name) throws AllocationInPlaceException{
        for(int i = 0; i < objectsAmount; i++){
            if(objects[i].getName().equals(name)){
                objectsAmount -= 1;
                sumOfSizes -= objects[i].getSizeValue();
                objects[i] = null;
                int pointer = -1;

                // переместить влево объекты, которые, возмонжно, были правее удаленного
                for(int j = 0; j < objectsAmount + 1; j++){
                    if(pointer != -1){
                        objects[j-1] = objects[j];
                    }
                    if(objects[j] == null){
                        pointer = j;
                    }
                }
                return;
            }
        }
        throw new AllocationInPlaceException("Невозможно удалить из \"" + getName() + "\" объект с именем \"" + name +
                "\", нет такого объекта.");
    }

    public SearchableThing getObject(String name) throws AllocationInPlaceException{
        for(SearchableThing object : objects){
            if(object.getName().equals(name)){
                return object;
            }
        }
        throw new AllocationInPlaceException("Невозможно получить объект с именем \"" + name + "\" в \"" + getName()
                + "\", нет объекта с таким именем.");
    }

    public SearchableThing getObject(int index) throws AllocationInPlaceException{
        if(index < 0 || index >= maxObjectsAmount || index >= objectsAmount){
            throw new AllocationInPlaceException("Невозможно получить объект с индексом: " + index +
                    ", некорректный индекс объекта.");
        } else {
            return objects[index];
        }
    }

    public int getObjectsAmount() {
        return objectsAmount;
    }

    public int getMaxObjectsAmount(){
        return maxObjectsAmount;
    }

    public boolean isHidden(){
        return isHidden;
    }

    public void Hide(){
        this.isHidden = true;
    }

    public void Show(){
        this.isHidden = false;
    }

    @Override
    public int hashCode(){
        int hC = getName().hashCode();

        for(int i = 0; i < maxObjectsAmount; i++){
            hC += objects[i].getName().hashCode();
        }

        if(isHidden){
            hC += 10000;
        } else {
            hC += 100000;
        }
        return hC;
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Place){
            if(((Place) object).isHidden() == isHidden &&
                    ((Place) object).getName().equals(getName()) &&
                    ((Place) object).getSizeValue() == this.getSizeValue() &&
                    ((Place) object).maxObjectsAmount == maxObjectsAmount &&
                    ((Place) object).getObjectsAmount() == objectsAmount){
                for(int i = 0; i < objectsAmount; i++){
                    try {
                        if (!objects[i].equals(((Place) object).getObject(i))) {
                            return false;
                        }
                    } catch (AllocationInPlaceException aipe){
                        System.out.println(aipe.getMessage());
                    }
                }
                return true;
            }
        }
        return false;
    }
}
