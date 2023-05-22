package subway.exception.input;

public class InvalidNewSectionDistanceException extends InputException {

    private final static String NEW_SECTION_DISTANCE_OVER_MESSAGE = "새로 등록되는 구간의 길이느 기존 구간의 길이보다 작아야합나디.";

    public InvalidNewSectionDistanceException() {
        super(NEW_SECTION_DISTANCE_OVER_MESSAGE);
    }
}
