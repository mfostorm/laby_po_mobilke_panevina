public class CalculatorUtils {

    // Произведение двух целых чисел
    public static int multiply(int a, int b) {
        return a * b;
    }

    // true, если число чётное
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    // Наибольшее из трёх дробных чисел
    public static double getMax(double a, double b, double c) {
        double max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        return max;
    }

    // Строка приветствия
    public static String generateGreeting(String name, int age) {
        return "Привет, " + name + "! Тебе уже " + age + " лет!";
    }

    // Квадрат из * размером size x size (size от 1 до 10)
    public static void printSquare(int size) {
        if (size < 1 || size > 10) {
            System.out.println("size должен быть от 1 до 10");
            return;
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    // Задача 4
    public static boolean isDiscountAvailable(boolean isRegularCustomer,
                                              double purchaseAmount) {
        if (isRegularCustomer || purchaseAmount > 1000.0) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(multiply(6, 7));
        System.out.println(isEven(10) + " " + isEven(7));
        System.out.println(getMax(2.5, 9.1, 4.0));
        System.out.println(generateGreeting("Анна", 20));
        printSquare(3);
        System.out.println(isDiscountAvailable(false, 1500.0) + " "
                + isDiscountAvailable(false, 500.0));
    }
}
