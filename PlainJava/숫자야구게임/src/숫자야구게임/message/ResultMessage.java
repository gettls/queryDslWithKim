package 숫자야구게임.message;

public enum ResultMessage {
	BALL_1 { 
		public void outputMessage() { 
			System.out.println("1볼"); 
		}
	},
	BALL_2 { public void outputMessage() { System.out.println("2볼"); }},
	BALL_3{ public void outputMessage() { System.out.println("3볼"); }},
	STRIKE_1{ public void outputMessage() { System.out.println("1스트라이크"); }},
	STRIKE_2{ public void outputMessage() { System.out.println("2스트라이크"); }},
	STRIKE_3{ public void outputMessage() { System.out.println("3스트라이크"); }};
	
	public abstract void outputMessage();

//	private final String message;
//	
//	private ResultMessage(String message) {
//		this.message = message;
//	}
//	
//	private String getMessage() {
//		return message;
//	}
}
