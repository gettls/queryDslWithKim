package ���ھ߱�����.message;

public enum ProgressMessage {
	
	WIN("3���� ���ڸ� ��� �����̽��ϴ�! ���� ����"),
	INPUT("���ڸ� �Է����ּ��� : "),
	TERMINATE("������ ���� �����Ϸ��� 1, �����Ϸ��� 2�� �Է��ϼ���.");
	
	private String message;
	
	private ProgressMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
