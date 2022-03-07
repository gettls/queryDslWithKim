package 숫자야구게임.message;

public enum StrikeMessage{

	STRIKE1("1스트라이크",1),
	STRIKE2("2스트라이크",2),
	STRIKE3("3스트라이크",3);
	
	private final String message;
	private final int numberOfCount;
	
	StrikeMessage(String message, int numberOfCount) {
		this.message = message;
		this.numberOfCount = numberOfCount;
	}
	
	public static void getMessage(int number) {
		for(StrikeMessage sm : StrikeMessage.values())
			if(sm.numberOfCount == number) System.out.println(sm.message);
	}
}
