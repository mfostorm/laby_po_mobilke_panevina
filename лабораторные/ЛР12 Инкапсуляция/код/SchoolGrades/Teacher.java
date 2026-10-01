package com.example.schoolgrades;

public class Teacher {

    void improveGrade(Student student) {
        int grade = student.getGrade();
        if (grade < 5) {          // выше пятёрки не повышаем
            grade = grade + 1;
        }
        student.setGrade(grade);
    }
}
