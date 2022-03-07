package ���ھ߱�����.message;

public enum StrikeMessage{

	STRIKE1("1��Ʈ����ũ",1),
	STRIKE2("2��Ʈ����ũ",2),
	STRIKE3("3��Ʈ����ũ",3);
	
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
