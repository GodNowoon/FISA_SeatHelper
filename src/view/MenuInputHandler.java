// 사용자 입력 처리 전담
package view;

import java.util.Scanner;

public class MenuInputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public int getUserChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
            	System.out.print("숫자를 입력하세요: ");
            	//e.printStackTrace();
            }
        }
    }
}
