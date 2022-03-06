package 숫자야구게임.service;

import java.util.Scanner;

import 숫자야구게임.message.ProgressMessage;
import 숫자야구게임.message.ResultMessage;

public class InputService {

	private Scanner scanner;
	private int answer;
	private int startOrEnd;
	
	public InputService(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public int getAnswer() {
		return answer;
	}

	public int getStartOrEnd() {
		return startOrEnd;
	}

	public void inputAnswer() {
		System.out.println(ProgressMessage.INPUT.getMessage());
		answer = scanner.nextInt();
	}
	
	public void inputStartOrEnd() {
		System.out.println(ProgressMessage.TERMINATE.getMessage());
		startOrEnd = scanner.nextInt();
	}
}
