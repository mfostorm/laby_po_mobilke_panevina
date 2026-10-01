package com.example.schoolgrades;

import android.util.Log;

public class Student {
    private int grade;   // оценка от 1 до 5

    public int getGrade() {
        return grade;
    }

    public void setGrade(int newGrade) {
        if (newGrade < 1 || newGrade > 5) {
            Log.w("Student", "Оценка должна быть от 1 до 5: " + newGrade);
            return;
        }
        grade = newGrade;
    }
}
