package subway.application.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;

import java.util.stream.Collectors;

@Repository
public class JdbcPathRepository implements PathRepository {

    private final SectionDao sectionDao;

    public JdbcPathRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Sections findAllSections() {
        return new Sections(
                sectionDao.findAll().stream()
                        .map(sectionEntity -> new Section(new Station(sectionEntity.getUpStation()), new Station(sectionEntity.getDownStation()), sectionEntity.getDistance()))
                        .collect(Collectors.toList())
        );
    }
}
