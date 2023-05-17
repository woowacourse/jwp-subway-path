package subway.repository;

import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;

public class LineWithSectionEntity {
    private final LineEntity lineEntity;
    private final SectionEntity sectionEntity;

    public LineWithSectionEntity(final LineEntity lineEntity, final SectionEntity sectionEntity) {
        this.lineEntity = lineEntity;
        this.sectionEntity = sectionEntity;
    }

    public LineEntity getLineEntity() {
        return lineEntity;
    }

    public SectionEntity getSectionEntity() {
        return sectionEntity;
    }
}
