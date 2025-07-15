package view;

import database.Student;

public class StudentPrinter {
    public void print(Student stu) {
    	System.out.println(
    		    String.format("|%2d | %-6s | %-4d | %-4s |",
    		        stu.getNo(),
    		        stu.getName() + (stu.isGlass() ? " 👓" : "  "),
    		        stu.getAge(),
    		        stu.getMbti()
    		    )
    		);
    }
}
