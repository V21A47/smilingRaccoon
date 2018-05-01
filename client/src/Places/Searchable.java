package Places;

public interface Searchable {
    default SearchableThing search(Place place, String name){

        if(!name.equals("")) {
            System.out.println(this + " ведет поиск объекта \"" + name + "\" в \"" + place + "\".");
        } else {
            System.out.println(this + " узнает, что находится в \"" + place + "\":");
        }

        if(place.isHidden()){
            System.out.println("Никто не знает, где найти \"" + place + "\", чтобы вести там поиск.");
            return null;
        }

        if(place.getObjectsAmount() == 0){
            System.out.println("Но \"" + place + "\" не содержит в себе никаких объектов.");
            return null;
        }

        try {
            for (int i = 0; i < place.getObjectsAmount(); i++) {
                SearchableThing objectInPlace = place.getObject(i);
                if (name.equals("")) {
                    if( ! (objectInPlace instanceof Place && ((Place) objectInPlace).isHidden()) ){
                        System.out.println("В \"" + place + "\" находится \"" + objectInPlace + "\"");
                    }
                } else {
                    if(objectInPlace.getName().equals(name) && !(objectInPlace instanceof Place && ((Place) objectInPlace).isHidden())){
                        System.out.println("Объект \"" + objectInPlace + "\" был найден.");
                        return objectInPlace;
                    }
                }
            }
            if(!name.equals("")){
                System.out.println("Но объект \"" + name + "\" найден не был.");
            }
        } catch (AllocationInPlaceException aipe){
            System.out.println(aipe.getMessage());
        }
        return null;
    }
}
