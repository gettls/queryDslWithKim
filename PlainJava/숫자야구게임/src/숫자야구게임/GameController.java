package 숫자야구게임;

import java.util.Scanner;

import 숫자야구게임.service.HintService;
import 숫자야구게임.service.InputService;

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
