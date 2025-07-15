package view;

import static view.AnsiColor.RESET;
import static view.AnsiColor.YELLOW;
import static view.AnsiColor.BLUE;

import java.util.Scanner;

public class MenuInputHandler {
	
    private final Scanner scanner = new Scanner(System.in);
    
    // 사용자 메뉴 입력
    public int getUserMenuChoice() {
    	
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
            	System.out.print(YELLOW + "⚠️ 숫자를 입력하세요: ");
            	//e.printStackTrace();
            }
        }
        
    }
    
    
    // 사용자 자리 저장 여부 입력
    public boolean getUserSaveChoice() {
    	
        while (true) {
            System.out.print(BLUE + "자리 배치를 저장하시겠습니까? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println(YELLOW + "⚠️  y 또는 n만 입력하세요." + RESET);
            }
        }
        
    }
    
}
