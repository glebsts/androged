package ee.ut.ta.search.ged.exceptions;

@SuppressWarnings("serial")
public class StoreFullException extends RuntimeException {
    public StoreFullException() {
        super();
    }

    public StoreFullException(String pDetailMessage,
                    Throwable pThrowable) {
        super(pDetailMessage, pThrowable);
    }

    public StoreFullException(String pDetailMessage) {
        super(pDetailMessage);
    }

    public StoreFullException(Throwable pThrowable) {
        super(pThrowable);
    }
}
