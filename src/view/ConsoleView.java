package view;

import static view.AnsiColor.BLUE;
import static view.AnsiColor.BOLD;
import static view.AnsiColor.CYAN;
import static view.AnsiColor.GREEN;
import static view.AnsiColor.PURPLE;
import static view.AnsiColor.RED;
import static view.AnsiColor.RESET;
import static view.AnsiColor.YELLOW;

public class ConsoleView {
	
    private final MenuInputHandler inputHandler;
    private final SeatPrinter seatPrinter;

    public ConsoleView() {
        inputHandler = new MenuInputHandler();
        seatPrinter = new SeatPrinter();
    }

    public void showMainMenu() {
        System.out.println();
        System.out.println(RED + " ,---.  ,------.  ,---. ,--------.    ,--.  ,--.,------.,--.   ,------. ,------.,------.  " + RESET);
        System.out.println(YELLOW + "'   .-' |  .---' /  O  '\\--.  .--'    |  '--'  ||  .---'|  |   |  .--. '|  .---'|  .--. ' " + RESET);
        System.out.println(GREEN + "`.  `-. |  `--, |  .-.  |  |  |       |  .--.  ||  `--, |  |   |  '--' ||  `--, |  '--'.' " + RESET);
        System.out.println(CYAN + ".-'    ||  `---.|  | |  |  |  |       |  |  |  ||  `---.|  '--.|  | --' |  `---.|  |\\  \\ " + RESET);
        System.out.println(PURPLE + "`-----' `------'`--' `--'  `--'       `--'  `--'`------'`-----'`--'     `------'`--' '--' " + RESET);
        System.out.println(BLUE + "=========================================================================================" + RESET);
        System.out.println(BOLD + "                             우리 FISA 자리 배치 프로그램" + RESET);
        System.out.println(BLUE + "=========================================================================================" + RESET);
        System.out.println(YELLOW + " 1. 모든 수강생 보기");
        System.out.println(YELLOW + " 2. 랜덤 자리 배치 보기");
        System.out.println(YELLOW + " 3. 현재 자리 보기");
        System.out.println(" 0. 종료" + RESET);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.print(" 메뉴를 선택하세요 " + GREEN + "▶ " + RESET);
    }

    // 사용자 메뉴 입력
    public int getUserChoice() {
        return inputHandler.getUserMenuChoice();
    }
    // 사용자 저장 여부 입력
    public boolean askSave() {
    	return inputHandler.getUserSaveChoice();
    }
    // 자리 출력
    public void printSeatLayout(String[][] seatArray) {
        seatPrinter.print(seatArray);
    }
    // 메세지 출력
    public void printMessage(String msg) {
        System.out.println(msg);
    }

}
