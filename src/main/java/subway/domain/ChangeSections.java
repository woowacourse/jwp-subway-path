package subway.domain;

public class ChangeSections {

    private final ChangeSectionStatus status;
    private Section updateSection;
    private Section insertOrRemoveSection;

    private ChangeSections(final ChangeSectionStatus status, final Section updateSection,
                           final Section insertOrRemoveSection) {
        this.status = status;
        this.updateSection = updateSection;
        this.insertOrRemoveSection = insertOrRemoveSection;
    }

    public ChangeSections(final ChangeSectionStatus status, final Section insertOrRemoveSection) {
        this.status = status;
        this.insertOrRemoveSection = insertOrRemoveSection;
    }

    public static ChangeSections makeChangeSectionsForUpdateSectionsByStatus(final ChangeSectionStatus status,
                                                                             final Section updateSection,
                                                                             final Section newSection) {
        return new ChangeSections(status, updateSection, newSection);
    }

    public static ChangeSections makeChangeSectionsForUpdateEdgeSectionsByStatus(final ChangeSectionStatus status,
                                                                                 final Section newSection) {
        return new ChangeSections(status, newSection);
    }

    public Section getUpdateSection() {
        return updateSection;
    }

    public Section getInsertOrRemoveSection() {
        return insertOrRemoveSection;
    }

    public boolean is(final ChangeSectionStatus changeSectionStatus) {
        return this.status == changeSectionStatus;
    }

}
