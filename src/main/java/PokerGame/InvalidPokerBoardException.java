package PokerGame;

public class InvalidPokerBoardException extends RuntimeException {
    public InvalidPokerBoardException(String message) {
        super(message);
    }
    public InvalidPokerBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    // Конструктор с причиной
    public InvalidPokerBoardException(Throwable cause) {
        super(cause);
    }

    // Конструктор по умолчанию
    public InvalidPokerBoardException() {
        super("Invalid poker board detected.");
    }
}
