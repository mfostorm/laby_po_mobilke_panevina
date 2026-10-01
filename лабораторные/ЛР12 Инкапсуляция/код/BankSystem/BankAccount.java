package com.example.banksystem;

import android.util.Log;

public class BankAccount {
    private double balance;

    public double getBalance() {
        return balance;
    }

    // Отрицательный баланс не допускается
    public void setBalance(double newBalance) {
        if (newBalance < 0) {
            Log.w("BankAccount", "Баланс не может быть отрицательным: " + newBalance);
            return;
        }
        balance = newBalance;
    }
}
