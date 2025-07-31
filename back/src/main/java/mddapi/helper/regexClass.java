package mddapi.helper;

import java.util.regex.Pattern;

public class regexClass {
	private final static String PASSWORD_CHECKER = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$";
	private final static String EMAIL_CHECKER = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	
	public static boolean checkEmail(String toTest) {
		return Pattern.matches(EMAIL_CHECKER, toTest);
	}
	public static boolean checkPassword(String toTest) {
		return Pattern.matches(PASSWORD_CHECKER, toTest);
	}
}
