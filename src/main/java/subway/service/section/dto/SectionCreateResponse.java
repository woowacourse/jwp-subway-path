package subway.service.section.dto;

import java.util.List;

public class SectionCreateResponse {

    private final long lindId;
    private final List<SectionResponse> addedSections;
    private final List<SectionResponse> deletedSections;

    public SectionCreateResponse(long lindId, List<SectionResponse> addedSections, List<SectionResponse> deletedSections) {
        this.lindId = lindId;
        this.addedSections = addedSections;
        this.deletedSections = deletedSections;
    }

    public long getLindId() {
        return lindId;
    }

    public List<SectionResponse> getAddedSections() {
        return addedSections;
    }

    public List<SectionResponse> getDeletedSections() {
        return deletedSections;
    }
}
