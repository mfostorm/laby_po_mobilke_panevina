import java.util.HashMap;

class Animal {
    void makeSound() { System.out.println("..."); }
}

class Dog extends Animal {
    @Override void makeSound() { System.out.println("Гав"); }
}

class Elephant extends Animal {
    @Override void makeSound() { System.out.println("Ту-ту"); }
    void trumpet() { System.out.println("Слон трубит!"); }
}

class Character {
    String name;
    int health;
    Character(String name, int health) { this.name = name; this.health = health; }
}

public class Demo {
    public static void main(String[] args) {
        // Полиморфный массив
        Animal[] zoo = { new Dog(), new Elephant() };
        for (Animal a : zoo) {
            a.makeSound();                    // вызывается версия подкласса
            if (a instanceof Elephant) {      // проверяем реальный тип
                ((Elephant) a).trumpet();     // и приводим тип вниз
            }
        }

        // HashMap: ключ → объект
        HashMap<String, Character> heroes = new HashMap<>();
        heroes.put("Geralt", new Character("Geralt", 100));
        heroes.put("Ciri", new Character("Ciri", 80));

        Character c = heroes.get("Ciri");     // поиск по ключу
        System.out.println(c.name + ": " + c.health);
    }
}
