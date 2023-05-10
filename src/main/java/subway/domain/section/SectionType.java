package subway.domain.section;

public enum SectionType {
    UPWARD("상행 종점"), NORMAL("일반 역");

    private final String description;

    SectionType(final String description) {
        this.description = description;
    }

    public static SectionType from(final String type) {
        return SectionType.valueOf(type);
    }
}
