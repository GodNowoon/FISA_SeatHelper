// 사용자 입력 처리 전담
package view;

import java.util.Scanner;

public class MenuInputHandler {
    private final Scanner scanner = new Scanner(System.in);
    
    // 사용자 메뉴 입력
    public int getUserMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
            	System.out.print("⚠️ 숫자를 입력하세요: ");
            	//e.printStackTrace();
            }
        }
    }
    
    // 사용자 자리 저장 여부 입력
    public boolean getUserSaveChoice() {
        while (true) {
            System.out.print("자리 배치를 저장하시겠습니까? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("⚠️  y 또는 n만 입력하세요.");
            }
        }
    }
    
}
