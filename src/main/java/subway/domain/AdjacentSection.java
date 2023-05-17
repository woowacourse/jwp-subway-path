package subway.domain;

import java.util.List;
import java.util.function.Function;

import subway.entity.SectionEntity;
import subway.exception.InvalidInputException;

public final class AdjacentSection {
    
    private final SectionEntity sectionEntity;
    private final Direction direction;
    
    private AdjacentSection(final SectionEntity sectionEntity, final Direction direction) {
        this.sectionEntity = sectionEntity;
        this.direction = direction;
    }
    
    public static AdjacentSection filter(final List<SectionEntity> sectionEntities, final long baseStationId,
                                         final Direction direction) {
        if (direction.isDown()) {
            final SectionEntity tempSectionEntity = getSectionInDirection(sectionEntities, SectionEntity::getUpStationId, baseStationId);
            return new AdjacentSection(tempSectionEntity, direction);
        }
        final SectionEntity tempSectionEntity = getSectionInDirection(sectionEntities, SectionEntity::getDownStationId, baseStationId);
        return new AdjacentSection(tempSectionEntity, direction);
    }
    
    private static SectionEntity getSectionInDirection(final List<SectionEntity> sectionEntities, final Function<SectionEntity, Long> function,
                                                       final long baseStationId) {
        return sectionEntities.stream()
                .filter(section1 -> function.apply(section1) == baseStationId)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("좌우 구역이 존재하지 않습니다."));
    }
    
    public void validate(final int distance) {
        if (this.sectionEntity.getDistance() < distance) {
            throw new InvalidInputException("거리가 기존 구역 거리를 초과했습니다.");
        }
    }
    
    public List<SectionEntity> splitTwoSections(final long newSectionId, final int distance, final long lineId) {
        if (this.direction.isDown()) {
            final SectionEntity newSection1Entity = new SectionEntity(lineId, this.sectionEntity.getUpStationId(), newSectionId, distance);
            final SectionEntity newSection2Entity = new SectionEntity(lineId, newSectionId, this.sectionEntity.getDownStationId(),
                    this.sectionEntity.getDistance() - distance);
            return List.of(newSection1Entity, newSection2Entity);
        }
        final SectionEntity newSection1Entity = new SectionEntity(lineId, newSectionId, this.sectionEntity.getDownStationId(), distance);
        final SectionEntity newSection2Entity = new SectionEntity(lineId, this.sectionEntity.getUpStationId(), newSectionId,
                this.sectionEntity.getDistance() - distance);
        return List.of(newSection1Entity, newSection2Entity);
    }
    
    public SectionEntity getSection() {
        return this.sectionEntity;
    }
}
