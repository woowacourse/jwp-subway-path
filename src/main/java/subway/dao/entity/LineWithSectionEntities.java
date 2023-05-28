package subway.dao.entity;

import java.util.List;
import java.util.stream.Collectors;

public class LineWithSectionEntities {
    private final LineEntity lineEntity;
    private final List<SectionWithStationNameEntity> sectionEntities;

    public LineWithSectionEntities(final LineEntity lineEntity,
                                   final List<SectionWithStationNameEntity> sectionEntities) {
        this.lineEntity = lineEntity;
        this.sectionEntities = sectionEntities;
    }

    public static LineWithSectionEntities of(final LineEntity lineEntity,
                                             final List<LineWithSectionEntityAndStationEntity> lineWithSectionEntities
    ) {
        List<SectionWithStationNameEntity> sectionEntities = lineWithSectionEntities.stream()
                .map(LineWithSectionEntityAndStationEntity::getSectionEntity)
                .map(entity -> new SectionWithStationNameEntity(
                        entity.getSectionId(),
                        entity.getUpStationEntity(),
                        entity.getDownStationEntity(),
                        entity.getDistance()
                ))
                .collect(Collectors.toList());

        return new LineWithSectionEntities(lineEntity, sectionEntities);
    }

    public LineEntity getLineEntity() {
        return lineEntity;
    }

    public List<SectionWithStationNameEntity> getSectionEntities() {
        return sectionEntities;
    }
}
