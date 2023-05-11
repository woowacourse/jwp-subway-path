package subway.dto;

import subway.domain.Section;

public class RemoveResult {
    private RemoveIds removeIds;
    private UpdateSections updateSections;

    public RemoveResult() {
        removeIds = new RemoveIds();
        updateSections = new UpdateSections();
    }

    public void insertIdToRemoveIds(Long id) {
        removeIds.insert(id);
    }

    public void insertSectionToUpdateSections(Section section) {
        updateSections.insert(section);
    }

    public RemoveIds getRemoveIds() {
        return removeIds;
    }

    public UpdateSections getUpdateSections() {
        return updateSections;
    }
}
