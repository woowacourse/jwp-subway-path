package subway.domain.exception;

public enum ExceptionType {
    INVALID_DISTANCE,
    LINE_HAS_STATION,
    LINE_HAS_NO_SECTION,
    NON_EXISTENT_SECTION,
    SECTION_CAN_NOT_BE_SPLIT,
    NO_SOURCE,
    STATION_ALREADY_EXIST,
    STATION_NO_EXIST_IN_LINE,
    NO_EXISTENT_STATION,
    LINE_ALREADY_EXIST,
    LINE_DOES_NOT_EXIST,
    STATION_DOES_NOT_EXIST,
    NOT_UP_LINE_LAST_STOP,
    NOT_DOWN_LINE_LAST_STOP,
    NO_PATH,
    PATH_HAS_LOOP, 
    NO_LINE,
}
