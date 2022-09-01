package exceptions;

public class MalException extends Exception {
        private String input;
        private int errorStart, errorEnd;
        private int lineNumber;
}

