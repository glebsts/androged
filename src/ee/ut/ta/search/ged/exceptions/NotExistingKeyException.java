package ee.ut.ta.search.ged.exceptions;

@SuppressWarnings("serial")
public class NotExistingKeyException extends Exception {
    public NotExistingKeyException() {
        super();
    }

    public NotExistingKeyException(String pDetailMessage, Throwable pThrowable) {
        super(pDetailMessage, pThrowable);
    }

    public NotExistingKeyException(String pDetailMessage) {
        super(pDetailMessage);
    }

    public NotExistingKeyException(Throwable pThrowable) {
        super(pThrowable);
    }
}
