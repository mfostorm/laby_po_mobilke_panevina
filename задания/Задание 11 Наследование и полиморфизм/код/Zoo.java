// Animal и Elephant — по примеру из лекции
class Animal {
    int age;
    int weight;
    String type;
    int hungerLevel;

    public void eat() {
        hungerLevel--;
    }

    public void walk() {
        hungerLevel++;
    }
}

class Elephant extends Animal {
    public Elephant(int age, int weight) {
        this.age = age;
        this.weight = weight;
        this.type = "Elephant";
        this.hungerLevel = 2;
    }
}

class Lion extends Animal {
    public Lion(int age, int weight) {
        this.age = age;
        this.weight = weight;
        this.type = "Lion";
        this.hungerLevel = 5;
    }
}

public class Zoo {
    // Принимает любого наследника Animal
    static void feed(Animal animalToFeed) {
        animalToFeed.eat();
        System.out.println(animalToFeed.type + ": голод = " + animalToFeed.hungerLevel);
    }

    public static void main(String[] args) {
        feed(new Elephant(10, 4000));
        feed(new Lion(4, 190));
    }
}
