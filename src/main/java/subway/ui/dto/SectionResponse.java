package subway.ui.dto;

import subway.domain.Section;

public class SectionResponse {

    private final Long id;

    public SectionResponse(Long id) {
        this.id = id;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId());
    }

    public Long getId() {
        return id;
    }
}
