package Interpreter;

import AliveObjects.Human;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Реализует работу с коллекцией.
 *
 */
public class CollectionStorage {
    private String name;
    private String fileName;
    private LinkedHashSet<Human> set = new LinkedHashSet<>();

    public boolean isEmpty(){
        return set.isEmpty();
    }
    /**
     * @param name название коллекции,
     * @param fileName имя файла, который будет использоваться для методов load, save.
     */
    public CollectionStorage(String name, String fileName){
        this.name = name;
        this.fileName = fileName;

    }

    public String getName(){return name;}

    /**
     * Команда удаляет из коллекции переданного в качестве параметра объекта человека. Если его там нет, то будет
     * выведена ошибка.
     *
     * @param element объект человека, который нужно поместить в коллекцию
     */
    public void remove(Human element){
        if(! set.remove(element)){
            System.out.println(element.getName() + " can't be removed from the collection.");
        }
    }

    /**
     * Команда удаляет из коллекции объекты людей, номера сертификатов которых больше номера сертификата заданного
     * в параметре человека.
     *
     * @param element объект человека, возраст которого используется для проверки при удалении других объектов людей
     */
    public void remove_greater(Human element){
        Iterator<Human> iter = set.iterator();
        while(iter.hasNext()){
            Human temp = iter.next();
            if(temp.compareTo(element) > 0){
                iter.remove();
            }
        }
    }

    /**
     * Команда удаляет из коллекции объекты людей, номера сертификатов рождения которых меньше номера сертификата
     * заданного в качестве параметра человека.
     *
     * @param element объект человека, сертификат рождения которого используется для проверки при удалении других объектов людей
     */
    public void remove_lower(Human element){
        Iterator<Human> iter = set.iterator();
        while(iter.hasNext()){
            Human temp = iter.next();
            if(temp.compareTo(element) < 0){
                iter.remove();
            }
        }
    }

    /**
     * Команда добавляет в коллекцию переданного в качестве параметра человека. Если он уже был размещен в
     * этой колекции, то будет выведена ошибка.
     *
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add(Human element) {
        if (!set.add(element)) {
            System.out.println(element.getName() + " can't be added to the collection.");
        }
    }

    /**
     * Команда добавляет в коллекцию переданного в качестве параметра человека, если его сертификат рождения имеет
     * наибольший номер среди всех уже добавленных в коллекцию. В случае, если человек с таким сертификатом уже есть
     * в коллекции, будет выведена ошибка.
     *
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add_if_max(Human element) {
        Iterator<Human> iter = set.iterator();
        while(iter.hasNext()){
            Human temp = iter.next();
            if(temp.compareTo(element) >= 0){
                System.out.println(element + " can not be added to the collection");
                return;
            }
        }
        if(!set.add(element)){
            System.out.println(element + " can not be added to the collection");
        }
    }

    /**
     * Команда добавляет в коллекцию переданного в качестве параметра человека, если его сертификат рождения имеет
     * наименьший номер из всех, уже добавленных в коллекцию. Если человек с таким сертификатом уже был размещен в этой
     * колекции, то будет выведена ошибка.
     *
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add_if_min(Human element){
       Iterator<Human> iter = set.iterator();
       while(iter.hasNext()){
           Human temp = iter.next();
           if(temp.compareTo(element) <= 0){
               System.out.println(element + " can not be added to the collection");
               return;
           }
       }
       if(!set.add(element)){
           System.out.println(element + " can not be added to the collection");
       }
    }


    /**
     * Команда import импортирует все объекты из файла в коллекцию. При попытке передать уже имеющиеся в коллекции
     * объекты будет выведена ошибка.
     *
     * @param fileName имя файла, в котором должны находиться объекты (fileName.csv, например).
     */
    public void importFromFile(String fileName){
        ArrayList<Human> humans;
        try {
            humans = CSVManager.readFromFile(fileName);
        } catch (FileNotFoundException ex){
            System.out.println("File was not found");
            return;
        }
        if(humans != null) {
            for (Human human : humans){
                if(!set.add(human)){
                    System.out.println(human + " is already in the collection");
                }
            }
        } else {
            System.out.println("Error while getting data");
        }
    }

    /**
     * Команда info выводит информацию о текущем состоянии коллекции в формате:
     * название коллекции
     * количество объектов, содержащихся в ней
     *     n строк значений полей объекта n
     */
    public void info(){
        System.out.printf("Collection with name \"%s\" is %s.%n", name, set.getClass());
        System.out.printf("The size is %d.%n" , set.size());
        if(set.size() > 0) {
            System.out.println("Contains:");

            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Human temp = (Human)(iter.next());
                System.out.printf("\t name: %-10s\t age: %-3d\t type: %-14s\t weight: %-3.1f\t certificate num: %-20s%n",
                        temp.getName(), temp.getAge(), temp.getType(), temp.getSizeValue(), temp.getBirthCertificateNumber());
            }
        }
    }

    /**
     * Команда save сохраняет содержимое коллекции в файл.
     *
     */
    public void save(){
        CSVManager.clearFile(fileName);
        if(set.size() > 0){
            for(Human human : set) {
                CSVManager.writeToFile(human, fileName);
            }
        }
    }

    /**
     * Команда clear удаляет все элементы коллекции.
     */
    public void clear(){
        set.clear();
    }

    /**
     * Команда load очищает коллекцию и записывает в нее объекты, описанные  в файле.
     */
    public void load() {
        set.clear();
        ArrayList<Human> humans;
        try {
            humans = CSVManager.readFromFile(fileName);
        } catch (FileNotFoundException ex){
            System.out.println("File was not found");
            return;
        }
        if(humans != null) {
            for (Human human : humans){
                if(!set.add(human)){
                    System.out.println(human + " can not be added to the set");
                }
            }
        } else {
            System.out.println("Error while getting data");
        }
    }
}
