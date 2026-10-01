package com.example.banksystem;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BankAccount myAccount = new BankAccount();
        myAccount.setBalance(1000.0);

        Bank bank = new Bank();
        bank.addInterest(myAccount);

        Log.i("Info:", "Баланс после начисления процентов: " + myAccount.getBalance());
    }
}
