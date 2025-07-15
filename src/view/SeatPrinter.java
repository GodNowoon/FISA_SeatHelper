package view;

import static view.AnsiColor.*;

public class SeatPrinter {

    public void print(String[][] seats) {
        System.out.println(BOLD + "\n📌 자리 배치 결과" + RESET);
        System.out.println(CYAN + "=========================================================================================" + RESET);
        System.out.println();
        System.out.println(BOLD + CYAN + "               ┌────────────────────────[ 칠판 ]────────────────────────┐" + RESET);

        for (int row = 0; row < seats.length; row++) {
        	System.out.print((row + 1) + " │ ");
            for (int col = 0; col < seats[row].length; col++) {
            	
                String name = seats[row][col];
                if (name == null || name.isBlank()) {
                	name = "";
                }

                // 가운데 정렬된 이름 (7칸 고정)
                String centeredName = centerText(name, 5);

                if (col == 4) {
                    System.out.print(" │   │ "); 
                }

                System.out.print("[" + centeredName + "] ");
            }

            System.out.println();
        }

        System.out.println();
        System.out.println(CYAN + "=========================================================================================" + RESET);
    }
    

    // 가운데 정렬을 위한 헬퍼 메서드
    private String centerText(String text, int width) {
        if (text == null) {
        	text = "";
        }
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }
    
    
    // 현재 자리 배치
    public void printSeatLayout(String[][] seats) {
    	
        System.out.println("\n📌 현재 자리 배치\n");
        for (String[] row : seats) {
            for (String seat : row) {
                System.out.printf("[%s] ", seat);
            }
            System.out.println();
        }
        System.out.println();
        
    }

}
