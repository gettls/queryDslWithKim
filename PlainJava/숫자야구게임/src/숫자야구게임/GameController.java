package ���ھ߱�����;

import java.util.Scanner;

import ���ھ߱�����.service.HintService;
import ���ھ߱�����.service.InputService;

public class GameController {

	private HintService hintService;
	private InputService inputService;
	private boolean running;
	private final int RESTART = 2;
	private final int EXIT = 1;
	
	public GameController(Scanner scanner) {
		inputService = new InputService(scanner);
		hintService = new HintService();
		running = true;
	}
	
	public void run() {
		while(running) {
			inputService.inputAnswer();
			if(hintService.checkAnswerAndPrintResult(inputService.getAnswer())){
				inputService.inputRestartOrExit();
			}
		}
	}
	
	
}
