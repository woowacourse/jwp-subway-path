package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.SectionDao;
import subway.dao.SectionStationDao;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Distance.from;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import({SectionDao.class, StationDao.class, SectionStationDao.class,
        StationRepository.class, SectionRepository.class})
class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;

    private Section 잠실_잠실새내;
    private Section 잠실새내_종합운동장;

    @BeforeEach
    void init() {
        final Station 잠실역 = 역을_저장한다("잠실역");
        final Station 잠실새내역 = 역을_저장한다("잠실새내역");
        final Station 종합운동장 = 역을_저장한다("종합운동장역");
        잠실_잠실새내 = new Section(from(10), 잠실역, 잠실새내역, 1L);
        잠실새내_종합운동장 = new Section(from(10), 잠실새내역, 종합운동장, 1L);
    }

    @Test
    void 저장한다() {
        // when
        final Long sectionId = sectionRepository.insert(잠실_잠실새내);

        // then
        assertThat(sectionId).isNotNull();
    }

    @Test
    void 삭제한다() {
        // given
        final Long insertId = sectionRepository.insert(잠실_잠실새내);

        // when
        sectionRepository.delete(insertId);

        // then
        assertThat(sectionRepository.findAll().getSections()).isEmpty();
    }

    @Test
    void 노선_아이디로_조회한다() {
        // when
        sectionRepository.insert(잠실_잠실새내);
        sectionRepository.insert(잠실새내_종합운동장);
        final SingleLineSections singleLineSections = sectionRepository.findAllByLineId(1L);

        // then
        assertAll(
                () -> assertThat(singleLineSections.getSections()).hasSize(2),
                () -> assertThat(singleLineSections.findAllStationsByOrder())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(new Station("잠실역"), new Station("잠실새내역"), new Station("종합운동장역"))
        );
    }

    @Test
    void 전체_조회한다() {
        // when
        sectionRepository.insert(잠실_잠실새내);
        sectionRepository.insert(잠실새내_종합운동장);
        final MultiLineSections multiLineSections = sectionRepository.findAll();

        // then
        assertAll(
                () -> assertThat(multiLineSections.getSections()).hasSize(2),
                () -> assertThat(multiLineSections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역"),
                () -> assertThat(multiLineSections.getSections().get(1).getDownStation().getName()).isEqualTo("종합운동장역")
        );
    }

    @Test
    void 수정한다() {
        // given
        final Long insertId = sectionRepository.insert(잠실_잠실새내);
        final Station 강남역 = 역을_저장한다("강남역");
        final Station 사당역 = 역을_저장한다("사당역");
        final Section updateSection = new Section(insertId, from(20), 강남역, 사당역, 1L);

        // when
        sectionRepository.update(updateSection);

        // then
        final Section result = sectionRepository.findAllByLineId(1L).getSections().get(0);

        assertAll(
                () -> assertThat(result.getDistance()).isEqualTo(from(20)),
                () -> assertThat(result.getUpStation().getName()).isEqualTo("강남역"),
                () -> assertThat(result.getDownStation().getName()).isEqualTo("사당역")
        );
    }

    @Test
    void 수정_삽입을_반영한다() {
        // given
        final Long insertId = sectionRepository.insert(잠실_잠실새내);
        final Station 강남역 = 역을_저장한다("강남역");
        final Station 사당역 = 역을_저장한다("사당역");
        final Section updateSection = new Section(insertId, from(20), 강남역, 사당역, 1L);

        // when, then
        assertThat(sectionRepository.insertAndUpdate(잠실새내_종합운동장, updateSection)).isNotNull();
    }


    private Station 역을_저장한다(String stationName) {
        final Station station = new Station(stationName);
        return stationRepository.insert(station);
    }
}
