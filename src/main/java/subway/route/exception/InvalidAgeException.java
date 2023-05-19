package subway.route.exception;

public class InvalidAgeException extends RuntimeException {

    public InvalidAgeException(int age) {
        super("나이는 0 이상의 값입니다. age: " +age);
    }
}
