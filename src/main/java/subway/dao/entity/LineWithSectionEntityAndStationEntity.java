package subway.dao.entity;

public class LineWithSectionEntityAndStationEntity {
    private final LineEntity lineEntity;
    private final SectionWithStationNameEntity sectionEntity;

    public LineWithSectionEntityAndStationEntity(final LineEntity lineEntity,
                                                 final SectionWithStationNameEntity sectionEntity) {
        this.lineEntity = lineEntity;
        this.sectionEntity = sectionEntity;
    }

    public LineEntity getLineEntity() {
        return lineEntity;
    }

    public SectionWithStationNameEntity getSectionEntity() {
        return sectionEntity;
    }
}
