package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationRepository = new StationRepository(stationDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);
    }

    @Test
    void 구간을_저장한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);

        // when
        sectionRepository.save(노선_ID, 구간);

        // then
        assertDoesNotThrow(
                () -> sectionRepository.findByLineIdAndUpStationAndDownStation(ID가_있는_시작역, ID가_있는_도착역, 노선_ID));
    }

    @Test
    void 존재하는_구간을_저장하면_예외가_발생한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);
        sectionRepository.save(노선_ID, 구간);

        // then
        assertThatThrownBy(() -> sectionRepository.save(노선_ID, 구간))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorMessage.DUPLICATE_SECTION.getErrorMessage());
    }

    @Test
    void 구간을_입력받아_구간을_삭제한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);
        sectionRepository.save(노선_ID, 구간);

        // then
        assertDoesNotThrow(() -> sectionRepository.delete(노선_ID, 구간));
    }

    @Test
    void 존재하지_않는_구간을_삭제하는_경우_예외가_발생한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);

        // then
        assertThatThrownBy(() -> sectionRepository.delete(노선_ID, 구간))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_SECTION.getErrorMessage());
    }

    @Test
    void 노선_ID를_입력받아_구간을_삭제한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);
        sectionRepository.save(노선_ID, 구간);

        // when
        sectionRepository.deleteByLineId(노선_ID);

        // then
        assertThat(sectionDao.findByLineId(노선_ID)).isEmpty();
    }

    @Test
    void 입력받은_노선_ID를_갖는_구간이_존재하지_않을_때_예외가_발생한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        // then
        assertThatThrownBy(() -> sectionRepository.deleteByLineId(노선_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_SECTION_BY_LINE_ID.getErrorMessage());
    }
}
