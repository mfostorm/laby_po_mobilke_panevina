package com.example.banksystem;

public class Bank {

    void addInterest(BankAccount account) {
        double balance = account.getBalance();
        balance = balance * 1.07;          // +7%
        account.setBalance(balance);
    }
}
