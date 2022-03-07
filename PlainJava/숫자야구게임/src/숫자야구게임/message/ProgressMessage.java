package 숫자야구게임.message;

public enum ProgressMessage {
	
	WIN("3개의 숫자를 모두 맞히셨습니다! 게임 종료"),
	INPUT("숫자를 입력해주세요 : "),
	TERMINATE("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
	
	private String message;
	
	private ProgressMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
