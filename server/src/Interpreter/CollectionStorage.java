package Interpreter;

import AliveObjects.Human;
import AliveObjects.HumanType;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.util.NoSuchElementException;


/**
 * Реализует работу с коллекцией.
 *
 */
public class CollectionStorage {
    private String name;
    private String fileName;
    private ConcurrentSkipListSet<Human> set = new ConcurrentSkipListSet<>(new Human.HumanComparator());

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
        if(!set.remove(element)){
            System.err.println(element.getName() + " can't be removed from the collection.");
        }
    }

    /**
     * 
     *
     * @param element объект человека, возраст которого используется для проверки при удалении других объектов людей
     */
    public void remove_greater(Human element){
        set.removeAll(set.stream()
                        .filter(s -> s.compareTo(element) > 0)
                        .collect(Collectors.toSet()));
    }

    /**
     *
     *
     * @param element объект человека, сертификат рождения которого используется для проверки при удалении других объектов людей
     */
    public void remove_lower(Human element){
        set.removeAll(set.stream()
                        .filter(s -> s.compareTo(element) < 0)
                        .collect(Collectors.toSet()));
    }

    /**
     * Команда добавляет в коллекцию переданного в качестве параметра человека. Если он уже был размещен в
     * этой колекции, то будет выведена ошибка.
     *
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add(Human element) {
        if (!set.add(element)) {
            System.err.println(element.getName() + " can't be added to the collection.");
        }
    }

    /**
     * 
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add_if_max(Human element) {
        try{
            if(element.compareTo(set.stream().max(Human::compareTo).get()) > 0){
                set.add(element);
            }
        } catch (NoSuchElementException e){
            set.add(element);
        }
    }

    /**
     * 
     * @param element человек, которого нужно поместить в коллекцию
     */
    public void add_if_min(Human element){
        try{
            if(element.compareTo(set.stream().min(Human::compareTo).get()) < 0){
                set.add(element);
            }
        } catch (NoSuchElementException e){
            set.add(element);
        }
    }


    /**
     * Команда import импортирует все объекты из файла в коллекцию. При попытке передать уже имеющиеся в коллекции
     * объекты будет выведена ошибка.
     *
     * @param fileName имя файла, в котором должны находиться объекты (fileName.csv, например).
     */
    public void importFromFile(String fileName){
        ArrayList<Human> humans = CSVManager.readFromFile(fileName);
        
        if(humans == null){
            System.err.println("No data was loaded from the file");
        } else {
            set.addAll(humans);
        }
    }

    /**
     * Команда info выводит информацию о текущем состоянии коллекции.
     */
    public void info(){
        System.out.printf("The size is %d.%n" , set.size());
        List<Human> list = set.stream()
                                    .collect(Collectors.toList());
        
        if(list.size() > 0) {
            System.out.println("Contains:");
            System.out.println("+---------------+-----+------+------------------+-----+-----+");
            System.out.println("|     name      | age | size |       type       |  x  |  y  |"); 
            System.out.println("+---------------+-----+------+------------------+-----+-----+");
            
            for (Human h : list) {
                System.out.printf("|%-15s|%-5d|%-6.1f|%-18s|%-5d|%-5d|\n",
                        h.getName(), h.getAge(), h.getSizeValue(), h.getType(), h.getX(), h.getY());
                System.out.println("+---------------+-----+------+------------------+-----+-----+");
            }
        }
    }

    /**
     * Команда save сохраняет содержимое коллекции в файл.
     *
     */
    public void save(){
        CSVManager.clearFile(fileName);
        set.stream().forEach( (p) -> CSVManager.writeToFile(p, fileName));
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
        importFromFile(fileName);
    }
}
