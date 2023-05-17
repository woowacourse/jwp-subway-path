package subway.dao.entity;

import java.util.List;
import java.util.stream.Collectors;
import subway.repository.LineWithSectionEntity;

public class LineWithSectionEntities {
    private final LineEntity lineEntity;
    private final List<SectionEntity> sectionEntities;

    public LineWithSectionEntities(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        this.lineEntity = lineEntity;
        this.sectionEntities = sectionEntities;
    }

    public static LineWithSectionEntities of(final LineEntity lineEntity,
                                             final List<LineWithSectionEntity> lineWithSectionEntities
    ) {
        List<SectionEntity> sectionEntities = lineWithSectionEntities.stream()
                .map(LineWithSectionEntity::getSectionEntity)
                .map(entity -> new SectionEntity(
                        entity.getId(),
                        entity.getUpStationId(),
                        entity.getDownStationId(),
                        entity.getLineId(),
                        entity.getDistance()
                ))
                .collect(Collectors.toList());

        return new LineWithSectionEntities(lineEntity, sectionEntities);
    }

    public LineEntity getLineEntity() {
        return lineEntity;
    }

    public List<SectionEntity> getSectionEntities() {
        return sectionEntities;
    }
}
