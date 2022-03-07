package ���ھ߱�����.message;

public enum BallMessage{
	
	BALL_1("1��",1),
	BALL_2("2��",2),
	BALL_3("3��",3);
	
	private final String message;
	private final int numberOfCount;
	
	BallMessage(String message, int numberOfCount) {
		this.message = message;
		this.numberOfCount = numberOfCount;
	}
	
	public static void getMessage(int number) {
		for(BallMessage bm : BallMessage.values())
			if(bm.numberOfCount == number) System.out.println(bm.message);
	}
}
