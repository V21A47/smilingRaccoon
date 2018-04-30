package AliveObjects;

import Groups.IllegalGroupOperation;

public interface Speakable {
    default void say(String text){
        checkObject();
        System.out.println(this + ": " + text);
    }

    default void send(Information data, Speakable receiver){
        checkObject();
        receiver.say("Получил информацию \"" + data + "\"");
        receiver.receive(data);
    }

    default void receive(Information data){
        checkObject();
        if(data.getImportance() >= 0.5D) {
            this.say("\"" + data + "\" по-настоящему важная информация!");
        }
    }

    void checkObject() throws IllegalGroupOperation;
}
