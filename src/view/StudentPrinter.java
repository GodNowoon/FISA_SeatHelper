package view;

import database.Student;

public class StudentPrinter {
    public void print(Student stu) {
        System.out.println("번호: " + stu.getNo());
        System.out.println("이름: " + stu.getName());
        System.out.println("출생연도: " + stu.getAge());
        System.out.println("MBTI: " + stu.getMbti());
        System.out.println("안경 여부: " + (stu.isGlass() ? "착용" : "미착용"));
        System.out.println("--------------------------");
    }
}
