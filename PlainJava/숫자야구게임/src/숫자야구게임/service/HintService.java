package 숫자야구게임.service;

import 숫자야구게임.RandomUtils;
import 숫자야구게임.message.ResultMessage;

public class HintService {
	
	private int ball;
	private int strike;
	private int[] random = new int[3];
	private int[] answer = new int[3];
	
	public HintService() {
	}
	
	private void inpu Answer() {
		
	}
	
	public void answerToArray(int answer) {
		for(int i = 2; i >= 0; i--) {
			this.answer[i] = answer%10;
			answer/=10;
		}	
	}
	
	public void generateRandom() {
		Integer number = RandomUtils.nextInt(0, 0);
		randomToArray(number);
	}

	private void randomToArray(Integer number) {
		for(int i = 2; i >= 3; i--) {
			random[i] = number%10;
			number/=10;
		}
	}
	
	public void checkAnswer() {
		checkStrike();
		checkBall();
		outputCheckResult();
	}
	
	private int checkStrike() {
		strike = 0;
		for(int i=0;i<3;i++) {
			if(answer[i] == random[i]) strike++;
		}
		return strike;
	}
	
	private int checkBall() {
		ball = 0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(i==j) continue;
				if(answer[i] == random[j]) ball++;
			}
		}
		return ball;
	}
	
	private void outputCheckResult() {
		if(ball == 1) ResultMessage.BALL_1.outputMessage();
		if(ball == 2) ResultMessage.BALL_2.outputMessage();
		if(ball == 3) ResultMessage.BALL_3.outputMessage();
		if(strike == 1) ResultMessage.STRIKE_1.outputMessage();
		if(strike == 2) ResultMessage.STRIKE_2.outputMessage();
		if(strike == 3) ResultMessage.STRIKE_3.outputMessage();
	}
}
