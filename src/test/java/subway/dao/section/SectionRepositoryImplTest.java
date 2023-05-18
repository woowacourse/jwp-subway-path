package subway.dao.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import subway.dao.line.LineDao;
import subway.dao.line.LineEntity;
import subway.dao.station.StationDao;
import subway.dao.station.StationEntity;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationDistance;

@SuppressWarnings("NonAsciiCharacters")
@Import({SectionDao.class, LineDao.class, StationDao.class, SectionRepositoryImpl.class})
@JdbcTest
class SectionRepositoryImplTest {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionRepository sectionRepositoryImpl;

    @Test
    void 구간_저장_테스트() {
        //given
        final Long savedLineId = lineDao.insert(new LineEntity("1호선", "파랑")).getLineId();
        final StationEntity firstStationEntity = stationDao.insert(new StationEntity("강남"));
        final StationEntity secondStationEntity = stationDao.insert(new StationEntity("역삼"));

        final Section section = new Section(
                firstStationEntity.toStation(),
                secondStationEntity.toStation(),
                new StationDistance(3)
        );

        //when
        sectionRepositoryImpl.save(section, savedLineId);

        //then
        final List<SectionEntity> all = sectionDao.findAll();
        assertThat(all).hasSize(1);
        final SectionEntity actualSection = all.get(0);
        assertThat(actualSection.getFirstStationId()).isEqualTo(firstStationEntity.getStationId());
    }

    @Test
    void 구간_저장시_참조하는_데이터가_존재하지_않으면_예외_발생() {
        //given
        final Long savedLineId = lineDao.insert(new LineEntity("1호선", "파랑")).getLineId();
        final StationEntity firstStationEntity = stationDao.insert(new StationEntity("강남"));

        final Section section = new Section(
                firstStationEntity.toStation(),
                new StationEntity("역삼").toStation(),
                new StationDistance(3)
        );

        //when
        assertThatThrownBy(() -> sectionRepositoryImpl.save(section, savedLineId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 구간_삭제_테스트() {
        //given
        lineDao.insert(new LineEntity("1호선", "파랑"));
        final StationEntity firstStationEntity = stationDao.insert(new StationEntity("강남"));
        final StationEntity secondStationEntity = stationDao.insert(new StationEntity("역삼"));

        final Section section = new Section(
                firstStationEntity.toStation(),
                secondStationEntity.toStation(),
                new StationDistance(3)
        );

        //when
        sectionRepositoryImpl.delete(section);

        //then
        final List<SectionEntity> all = sectionDao.findAll();
        assertThat(all).hasSize(0);
    }
}
