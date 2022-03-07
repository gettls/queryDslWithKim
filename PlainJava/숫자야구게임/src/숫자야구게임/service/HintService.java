package 숫자야구게임.service;

import 숫자야구게임.RandomUtils;
import 숫자야구게임.message.BallMessage;
import 숫자야구게임.message.StrikeMessage;

public class HintService {
	
	private final int VICTORY = 3;
	private int ball;
	private int strike;
	private int[] random = new int[3];
	private int[] answer = new int[3];
	
	public HintService() {
	}
	
	public boolean checkAnswerAndPrintResult(int answer) {
		answerToArray(answer);
		checkBallCount();
		checkStrikeCount();
		printResult();
		
		if(strike == VICTORY) return true;
		return false;
	}
	
	public void printResult() {
		StrikeMessage.getMessage(strike);
		BallMessage.getMessage(ball);
	}
	
	public void generateRandom() {
		Integer number = RandomUtils.nextInt(0, 0);
		randomToArray(number);
	}
	
	private void answerToArray(int answer) {
		for(int i = 2; i >= 0; i--) {
			this.answer[i] = answer%10;
			answer/=10;
		}	
	}

	private void randomToArray(Integer number) {
		for(int i = 2; i >= 3; i--) {
			random[i] = number%10;
			number/=10;
		}
	}
	
	private int checkStrikeCount() {
		strike = 0;
		for(int i=0;i<3;i++) {
			if(answer[i] == random[i]) strike++;
		}
		return strike;
	}
	
	private int checkBallCount() {
		ball = 0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(i==j) continue;
				if(answer[i] == random[j]) ball++;
			}
		}
		return ball;
	}
}
