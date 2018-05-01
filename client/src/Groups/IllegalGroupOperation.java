package Groups;

public class IllegalGroupOperation extends RuntimeException {
    public IllegalGroupOperation() {
        super();
    }

    public IllegalGroupOperation(String message) {
        super(message);
    }

    public IllegalGroupOperation(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalGroupOperation(Throwable cause) {
        super(cause);
    }


}
