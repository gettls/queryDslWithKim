package 숫자야구게임;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {
	private static final Random RANDOM = new Random();
	private static final List<Integer> list = new ArrayList<>();  
	
	private RandomUtils() {
    }
	
	public static int nextInt(final int startInclusive, final int endInclusive) {
		int result = 0;
		while(list.size() < 3) {
			Integer number = RANDOM.nextInt(8)+1;
			if(validate(number)) {
				list.add(number);
			}
			result = result * 10 + number;
		}
		return result;
	}
	
	private static boolean validate(int num) {
		return !list.stream().anyMatch(t -> t.equals(num));
	}
}
