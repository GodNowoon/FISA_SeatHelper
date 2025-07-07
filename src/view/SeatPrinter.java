package view;

import static view.AnsiColor.*;

public class SeatPrinter {

    public void print(String[][] seats) {
        System.out.println(BOLD + "\n📌 자리 배치 결과" + RESET);
        System.out.println(CYAN + "=========================================================================================" + RESET);
        System.out.println();
        System.out.println(BOLD + CYAN + "               ┌────────────────────────[ 칠판 ]────────────────────────┐" + RESET);

        for (int i = 0; i < seats.length; i++) {
        	System.out.print((i + 1) + " │ ");
            for (int j = 0; j < seats[i].length; j++) {
            	
                String name = seats[i][j];
                if (name == null || name.isBlank()) {
                	name = "";//null ?????
                }

                // 가운데 정렬된 이름 (7칸 고정)
                String centeredName = centerText(name, 5);

                if (j == 4) {
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
        if (text == null) text = "";
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }
}
