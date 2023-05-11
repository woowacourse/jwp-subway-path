package subway.application.section.dto;

import subway.domain.Section;

public class SectionResponse {

    private long sectionId;
    private String upStationName;
    private String downStationName;

    public SectionResponse(long sectionId, String upStationName, String downStationName) {
        this.sectionId = sectionId;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
    }

    public static SectionResponse of(Section section) {
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
