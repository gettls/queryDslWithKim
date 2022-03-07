package 숫자야구게임.service;

import java.util.Scanner;

import 숫자야구게임.message.ProgressMessage;

public class InputService {

	private Scanner scanner;
	private int restartOrExit;
	private int answer;
	
	public InputService(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public int getAnswer() {
		return answer;
	}

	public int getStartOrEnd() {
		return restartOrExit;
	}

	public void inputAnswer() {
		System.out.println(ProgressMessage.INPUT.getMessage());
		answer = scanner.nextInt();
	}
	
	public void inputRestartOrExit() {
		System.out.println(ProgressMessage.TERMINATE.getMessage());
		restartOrExit = scanner.nextInt();
	}
}
