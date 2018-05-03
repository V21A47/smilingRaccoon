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
    public String remove(Human element){
        if(!set.remove(element)){
            return (element + " can't be removed from the collection.");
        } else {
            return (element + " was deleted");
        }
    }
  
    /**
     * 
     *
     * @param element объект человека, возраст которого используется для проверки при удалении других объектов людей
     */
    public String remove_greater(Human element){
        int size = set.size();
        set.removeAll(set.stream()
                        .filter(s -> s.compareTo(element) > 0)
                        .collect(Collectors.toSet()));
        if(size == set.size()){
            return ("No elements which are greater than " + element);
        } else {
            return ("Elements which are greater than " + element + " were deleted.");
        }
    }

    /**
     *
     *
     * @param element объект человека, сертификат рождения которого используется для проверки при удалении других объектов людей
     */
    public String remove_lower(Human element){
        int size = set.size();
        set.removeAll(set.stream()
                        .filter(s -> s.compareTo(element) < 0)
                        .collect(Collectors.toSet()));
        if(size == set.size()){
            return ("No elements which are lower than " + element);
        } else {
            return ("Elements which are lower than " + element + " were deleted.");
        }
    }

    /**
     * Команда добавляет в коллекцию переданного в качестве параметра человека. Если он уже был размещен в
     * этой колекции, то будет выведена ошибка.
     *
     * @param element человек, которого нужно поместить в коллекцию
     */
    public String add(Human element) {
        if (!set.add(element)) {
            return (element + " can't be added to the collection.");
        } else {
            return (element + " was added to the collection.");
        }
    }

    /**
     * 
     * @param element человек, которого нужно поместить в коллекцию
     */
    public String add_if_max(Human element) {
        int size = set.size();
        
        try{
            if(element.compareTo(set.stream().max(Human::compareTo).get()) > 0){
                set.add(element);
            }
        } catch (NoSuchElementException e){
            set.add(element);
        }
        
        if(size == set.size()){
            return (element + " can't be added to the collection.");
        } else {
            return (element + " was added to the collection.");
        }
    }

    /**
     * 
     * @param element человек, которого нужно поместить в коллекцию
     */
    public String add_if_min(Human element){
        int size = set.size();
        
        try{
            if(element.compareTo(set.stream().min(Human::compareTo).get()) < 0){
                set.add(element);
            }
        } catch (NoSuchElementException e){
            set.add(element);
        }
        
        if(size == set.size()){
            return (element + " can't be added to the collection.");
        } else {
            return (element + " was added to the collection.");
        }
    }


    /**
     * Команда import импортирует все объекты из файла в коллекцию. При попытке передать уже имеющиеся в коллекции
     * объекты будет выведена ошибка.
     *
     * @param fileName имя файла, в котором должны находиться объекты (fileName.csv, например).
     */
    public String importFromFile(String fileName){
        ArrayList<Human> humans = CSVManager.readFromFile(fileName);
        
        int size = set.size();
        
        if(humans == null){
            return ("No data was loaded from the file.");
        } else {
            set.addAll(humans);
            if(size == set.size()){
                return("No data was loaded from the file.");
            }else{
                return ("Some objects were loaded from a file " + fileName);
            }
        }
    }

    /**
     * Команда info выводит информацию о текущем состоянии коллекции.
     */
    public String info(){
        StringBuilder s = new StringBuilder();
        
        s.append(("The size is " + set.size()) + "\n");
        List<Human> list = set.stream()
                                    .collect(Collectors.toList());
        
        if(list.size() > 0) {
            s.append("Contains:" + "\n");
            for (Human h : list) {
                s.append(h.getName() +" "+ h.getAge() +" "+ h.getSizeValue() +" "+ h.getType() +" "+ h.getX() +" "+ h.getY() + "\n");
            }
        }
        
        return(s.toString());
    }

    /**
     * Команда save сохраняет содержимое коллекции в файл.
     *
     */
    public String save(){
        CSVManager.clearFile(fileName);
        set.stream().forEach( (p) -> CSVManager.writeToFile(p, fileName));
        
        return("The collection was saved to the file " + fileName);
    }

    /**
     * Команда clear удаляет все элементы коллекции.
     */
    public String clear(){
        set.clear();
        return("All elements were deleted from the collection");
    }

    /**
     * Команда load очищает коллекцию и записывает в нее объекты, описанные  в файле.
     */
    public String load() {
        set.clear();
        importFromFile(fileName);
        
        if(set.size() > 0){
            return ("Some objects were loaded from a file " + fileName);
        } else {
            return ("No objects were loaded from a file " + fileName);
        }
    }
    
    public String story(){
        StringBuilder s = new StringBuilder();
        
        Human policeman = new Human("Pavel", 32, HumanType.POLICE, 0, 0);
        Human robber = new Human("Vasya", 20, HumanType.BANDIT, 10, 10);
        
        set.add(policeman);
        set.add(robber);
        
        s.append("Two persons do activity in this story:\n" + info());
        
        s.append("Robber is running away");
        
        //s.remove(robber);
        robber.move(20, 20);
        s.append(info());
        s.append("And faster");
        robber.move(100,100);
        s.append(info());
        s.append("But the policeman is faster");
        policeman.move(100,100);
        s.append(info());
        s.append("Game over");
        set.remove(robber);
        s.append(info());
        
        return(s.toString());
    }
}
