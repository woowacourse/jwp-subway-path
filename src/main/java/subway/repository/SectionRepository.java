package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionDetailEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findSectionsWithSort() {
        List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionsWithSort();
        return convertToSections(sectionDetailEntities);
    }

    private List<Section> convertToSections(final List<SectionDetailEntity> sectionDetailEntities) {
        return sectionDetailEntities.stream()
                .map(this::convertToSection)
                .collect(Collectors.toList());
    }

    private Section convertToSection(final SectionDetailEntity sectionDetailEntity) {
        return new Section(
                new Station(sectionDetailEntity.getUpStationId(), sectionDetailEntity.getUpStationName()),
                new Station(sectionDetailEntity.getDownStationId(), sectionDetailEntity.getDownStationName()),
                sectionDetailEntity.getDistance(),
                sectionDetailEntity.getOrder()
        );
    }
}
