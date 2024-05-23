package app.services;

public class UserEandPValidation {
    public static boolean checkUpperCase(String str) {
        char c;
        boolean upperCaseFlag = false;
        boolean lowerCaseFlag = false;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCaseFlag = true;
            } else if (Character.isLowerCase(c)) {
                lowerCaseFlag = true;
            }
            if (upperCaseFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    public static boolean checkLength(String str) {
        return str.length() < 129 && str.length() > 7;
    }

    public static boolean checkNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean validatePassword(String password) {
        return checkNumeric(password) && checkLength(password) && checkUpperCase(password);
    }

    public static boolean checkEmailAt(String email) {
        return email.contains("@");
    }
}
