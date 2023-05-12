package subway.application;

import org.springframework.stereotype.Component;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Section;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class SectionMapper {

    protected SectionEntity mapToEntity(Section section) {
        return new SectionEntity(
                section.getDistance().getDistance(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getLineId()
        );
    }
    protected List<Section> mapFrom(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(this::mapFrom)
                .collect(toList());
    }

    protected Section mapFrom(SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                new Distance(sectionEntity.getDistance()),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getLineId()
        );
    }
}
