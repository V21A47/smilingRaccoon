package Places;

public class AllocationInPlaceException extends Exception {
    public AllocationInPlaceException() {
        super();
    }

    public AllocationInPlaceException(String message) {
        super(message);
    }

    public AllocationInPlaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllocationInPlaceException(Throwable cause) {
        super(cause);
    }
}
