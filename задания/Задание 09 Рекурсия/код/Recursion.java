public class Recursion {

    // Задача 1: добавлен базовый случай
    public static int sum(int n) {
        if (n <= 0) {
            return 0;
        }
        return n + sum(n - 1);
    }

    public static int factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    // Задача 3: итеративная версия power
    public static int power(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }

    // Задача 4: исправленный mystery — движемся к базовому случаю
    public static void mystery(int n) {
        if (n <= 0) {
            System.out.println("Готово!");
            return;
        }
        System.out.println(n);
        mystery(n - 1);
    }

    public static void main(String[] args) {
        System.out.println("sum(5) = " + sum(5));
        System.out.println("factorial(4) = " + factorial(4));
        System.out.println("power(2, 5) = " + power(2, 5));
        mystery(3);
    }
}
