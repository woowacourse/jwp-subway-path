package subway2.application;

public class NotFoundException extends RuntimeException {

    public NotFoundException(){};

    public NotFoundException(String message) {
        super(message);
    }
}
