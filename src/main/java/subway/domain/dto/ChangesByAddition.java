package subway.domain.dto;

import java.util.List;

import subway.domain.Section;

public class ChangesByAddition {

    private final List<Section> addedSections;
    private final List<Section> deletedSections;

    public ChangesByAddition(List<Section> addedSections, List<Section> deletedSections) {
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
