public class BankAccount {

    // Приватные переменные экземпляра
    private String accountNumber;
    private double balance;
    private String ownerName;

    public BankAccount(String accountNumber, String ownerName, double balance) {
        // Имена параметров совпадают с полями — разрешаем конфликт через this
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной");
            return;
        }
        balance += amount;
    }

    // Возвращает true, если снятие прошло успешно
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            System.out.println("Недостаточно средств на счёте " + accountNumber);
            return false;
        }
        balance -= amount;
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public void transfer(BankAccount toAccount, double amount) {
        boolean success = withdraw(amount);   // локальная переменная
        if (success) {
            toAccount.deposit(amount);
            System.out.println(ownerName + " перевёл " + amount
                    + " → " + toAccount.ownerName);
        }
    }

    public static void main(String[] args) {
        BankAccount ivan = new BankAccount("40817-001", "Иван", 5000);
        BankAccount olga = new BankAccount("40817-002", "Ольга", 1000);

        ivan.deposit(1500);
        ivan.transfer(olga, 2000);
        olga.withdraw(10000);

        System.out.println("Иван: " + ivan.getBalance());
        System.out.println("Ольга: " + olga.getBalance());
    }
}
