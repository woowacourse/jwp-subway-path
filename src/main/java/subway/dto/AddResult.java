package subway.dto;

import subway.domain.Section;

public class AddResult {
    private AddSections addSections;
    private UpdateSections updateSections;

    public AddResult() {
        addSections = new AddSections();
        updateSections = new UpdateSections();
    }

    public void insertSectionToAddSections(Section section) {
        addSections.insert(section);
    }

    public void insertSectionToUpdateSections(Section section) {
        updateSections.insert(section);
    }

    public AddSections getAddSections() {
        return addSections;
    }

    public UpdateSections getUpdateSections() {
        return updateSections;
    }
}
