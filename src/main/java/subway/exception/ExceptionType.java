package subway.exception;

public enum ExceptionType {
    INVALID_DISTANCE("Distance should be bigger than 1"),
    LINE_IS_ALREADY_INITIALIZED("Already initialized can't be initialized again"),
    LINE_IS_NOT_INITIALIZED("Should initialize line first."),
    NON_EXISTENT_SECTION("Can't find section with sourceStationId, targetStationId. Please check sourceStationId, targetStationId"),
    SECTION_CAN_NOT_BE_SPLIT("Distance is too long. Distance should be shorter than origin section."),
    NOT_LAST_STOP("When you put 'null' for targetStationId, sourceStation must be last stop"),
    STATION_ALREADY_EXIST_IN_LINE("Station tried to put in line is already existed in line. please check again"),
    STATION_NO_EXIST_IN_LINE("Can't find stationId tried to delete in line"),
    NO_SOURCE("Can't find source station"),
    NO_EXISTENT_STATION("Can't find station"),
    UN_EXISTED_LINE("lineId is not existed in DB"),
    UN_EXISTED_STATION("stationId is not existed in DB"),
    LINE_NAME_ALREADY_EXISTED("That line name is already existed."),
    STATION_NAME_IS_ALREADY_EXISTED("That station name is already existed.");

    private final String reason;

    ExceptionType(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
