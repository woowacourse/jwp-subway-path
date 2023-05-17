package subway.service.section.dto;

import subway.service.section.domain.Section;

public class SectionResponse {

    private final long sectionId;
    private final String upStationName;
    private final String downStationName;

    public SectionResponse(long sectionId, String upStationName, String downStationName) {
        this.sectionId = sectionId;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation().getName(), section.getDownStation().getName());
    }

    public long getSectionId() {
        return sectionId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }
}
