package ee.ut.ta.search.ged.exceptions;

@SuppressWarnings("serial")
public class InvalidTypeException extends Exception {
    public InvalidTypeException() {
        super();
    }

    public InvalidTypeException(String pDetailMessage, Throwable pThrowable) {
        super(pDetailMessage, pThrowable);
    }

    public InvalidTypeException(String pDetailMessage) {
        super(pDetailMessage);
    }

    public InvalidTypeException(Throwable pThrowable) {
        super(pThrowable);
    }
}
