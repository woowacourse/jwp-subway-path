package subway.service.section.dto;

import subway.service.section.domain.Section;

import java.util.List;

public class DeleteResult {
    private final List<Section> addedSections;
    private final List<Section> deletedSections;
    private final boolean isLastSection;

    public DeleteResult(List<Section> addedSections, List<Section> deletedSections, boolean isLastSection) {
        this.addedSections = addedSections;
        this.deletedSections = deletedSections;
        this.isLastSection = isLastSection;
    }

    public List<Section> getAddedSections() {
        return addedSections;
    }

    public List<Section> getDeletedSections() {
        return deletedSections;
    }

    public boolean isLastSection() {
        return isLastSection;
    }
}
