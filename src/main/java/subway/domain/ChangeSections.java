package subway.domain;

public class ChangeSections {

    private final ChangeSectionStatus status;
    private Section updateSection;
    private Section newSection;

    private ChangeSections(final ChangeSectionStatus status, final Section updateSection, final Section newSection) {
        this.status = status;
        this.updateSection = updateSection;
        this.newSection = newSection;
    }

    public ChangeSections(final ChangeSectionStatus status, final Section newSection) {
        this.status = status;
        this.newSection = newSection;
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

    public boolean isChangeMiddle(final ChangeSectionStatus status) {
        return this.status.equals(status);
    }

    public Section getUpdateSection() {
        return updateSection;
    }

    public Section getNewSection() {
        return newSection;
    }

}
