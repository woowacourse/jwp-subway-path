package subway.service.section.dto;

import subway.service.section.domain.Section;

import java.util.List;

public class DeleteResult {
    private final List<Section> addedSections;
    private final List<Section> deletedSections;

    public DeleteResult(List<Section> addedSections, List<Section> deletedSections) {
        this.addedSections = addedSections;
        this.deletedSections = deletedSections;
    }

    public List<Section> getAddedSections() {
        return addedSections;
    }

    public List<Section> getDeletedSections() {
        return deletedSections;
    }
}
