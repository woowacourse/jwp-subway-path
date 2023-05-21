package subway.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.TestData._2호선;
import static subway.TestData.당곡역;
import static subway.TestData.보라매병원역;
import static subway.TestData.봉천역;
import static subway.TestData.신림선;
import static subway.TestData.신림선_당곡_신림_3;
import static subway.TestData.신림선_보라매병원_당곡_4;
import static subway.TestData.신림역;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import java.util.List;

@JdbcTest
@Sql("/schema.sql")
class SectionRepositoryTest {

    private SectionRepository sectionRepository;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate);
        this.lineDao = new LineDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
        this.sectionRepository = new SectionRepository(stationDao, lineDao, sectionDao);
    }

    private void insert_신림역_당곡역_신림선() {
        stationDao.insert(신림역);
        stationDao.insert(당곡역);
        lineDao.insert(신림선);
    }

    private void insert_신림선_당곡_신림_3() {
        sectionRepository.save(신림선_당곡_신림_3);
    }

    @Test
    void Section을_성공적으로_저장한다() {
        // given
        insert_신림역_당곡역_신림선();

        // when
        Section savedSection = sectionRepository.save(신림선_당곡_신림_3);

        // then
        assertAll(
                () -> assertThat(savedSection.getId()).isEqualTo(1L),
                () -> assertThat(savedSection.getLine()).isEqualTo(신림선),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(당곡역),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(신림역),
                () -> assertThat(savedSection.getDistance()).isEqualTo(new Distance(3))
        );
    }

    @Test
    void 여러_Section들을_성공적으로_저장한다() {
        // given
        insert_신림역_당곡역_신림선();
        stationDao.insert(보라매병원역);

        List<Section> sections = List.of(신림선_당곡_신림_3, 신림선_보라매병원_당곡_4);

        // when
        sectionRepository.saveSections(sections);

        // then
        assertThat(sectionRepository.findAll().getSections()).hasSize(2);
    }

    @Test
    void 저장되어_있지_않은_Line이나_Station이_포함된_Section을_저장하면_예외가_발생한다() {
        // given
        Section section = new Section(_2호선, 신림역, 봉천역, new Distance(10));

        // expect
        assertThatThrownBy(() -> sectionRepository.save(section))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void 모든_Section을_조회한다() {
        // given
        insert_신림역_당곡역_신림선();
        insert_신림선_당곡_신림_3();

        // when
        Sections sections = sectionRepository.findAll();
        Section savedSection = sections.getSections().get(0);

        // then
        assertAll(
                () -> assertThat(savedSection.getId()).isEqualTo(1L),
                () -> assertThat(savedSection.getLine()).isEqualTo(신림선),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(당곡역),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(신림역),
                () -> assertThat(savedSection.getDistance()).isEqualTo(new Distance(3))
        );
    }

    @Test
    void Section을_삭제한다() {
        // given
        insert_신림역_당곡역_신림선();
        insert_신림선_당곡_신림_3();

        // when
        sectionRepository.delete(신림선_당곡_신림_3);

        // then
        assertThat(sectionRepository.findAll().getSections()).hasSize(0);
    }

    @Test
    void 특정_Line에_존재하는_모든_Section을_반환한다() {
        // given
        insert_신림역_당곡역_신림선();
        insert_신림선_당곡_신림_3();

        // when
        Sections sections_신림선 = sectionRepository.findByLine(신림선);

        // then
        assertThat(sections_신림선.getSections()).hasSize(1);
    }

    @Test
    void 특정_라인의_Section_정보들을_수정한다() {
        // given
        insert_신림역_당곡역_신림선();
        insert_신림선_당곡_신림_3();
        stationDao.insert(보라매병원역);
        sectionRepository.save(신림선_보라매병원_당곡_4);

        // when
        Sections sections = sectionRepository.findAll();
        sections.removeStation(당곡역);

        sectionRepository.update(신림선.getId(), sections);

        // then
        assertThat(sectionRepository.findByLine(신림선).getSections()).hasSize(1);
    }
}
