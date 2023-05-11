package subway.domain;

public enum SectionCase {

    EMPTY_SECTIONS,
    EXIST_ONLY_UP_STATION,
    EXIST_ONLY_DOWN_STATION,

    END_POINT_STATION,
    MIDDLE_POINT_STATION;

    public static SectionCase findInsertCase(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation) {
            return EMPTY_SECTIONS;
        }
        if (isExistUpStation && !isExistDownStation) {
            return EXIST_ONLY_UP_STATION;
        }
        return EXIST_ONLY_DOWN_STATION;
    }

    public static SectionCase findDeleteCase(final int nearStationCount) {
        if (nearStationCount == 1) {
            return END_POINT_STATION;
        }

        return MIDDLE_POINT_STATION;
    }
}
