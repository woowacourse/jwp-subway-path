package subway.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import java.util.List;

@JdbcTest
@Sql("/schema.sql")
class SectionRepositoryTest {

    private SectionRepository sectionRepository;
    private StationDao stationDao;
    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Station 신림역;
    private Station 봉천역;
    private Line _2호선;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        this.lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        SectionDao sectionDao = new SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        this.sectionRepository = new SectionRepository(stationDao, lineDao, sectionDao);

        this.신림역 = new Station(1L, "신림");
        this.봉천역 = new Station(2L, "봉천");
        this._2호선 = new Line(1L, "2호선", "초록색", 0);
    }

    private void insert_신림역_봉천역_2호선() {
        stationDao.insert(신림역);
        stationDao.insert(봉천역);
        lineDao.insert(_2호선);
    }

    private void saveSection() {
        Section section = new Section(_2호선, 신림역, 봉천역, new Distance(10));
        sectionRepository.save(section);
    }

    @Test
    void Section을_성공적으로_저장한다() {
        // given
        insert_신림역_봉천역_2호선();
        Section section = new Section(_2호선, 신림역, 봉천역, new Distance(10));

        // when
        Section savedSection = sectionRepository.save(section);

        // then
        assertAll(
                () -> assertThat(savedSection.getId()).isEqualTo(1L),
                () -> assertThat(savedSection.getLine()).isEqualTo(_2호선),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(신림역),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(봉천역),
                () -> assertThat(savedSection.getDistance()).isEqualTo(new Distance(10))
        );
    }

    @Test
    void 여러_Section들을_성공적으로_저장한다() {
        // given
        insert_신림역_봉천역_2호선();

        Station 낙성대역 = new Station(3L, "낙성대역");
        stationDao.insert(낙성대역);

        List<Section> sections = List.of(
                new Section(_2호선, 신림역, 봉천역, new Distance(10)),
                new Section(_2호선, 봉천역, 낙성대역, new Distance(9))
        );

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
        insert_신림역_봉천역_2호선();
        saveSection();

        // when
        Sections sections = sectionRepository.findAll();
        Section savedSection = sections.getSections().get(0);

        // then
        assertAll(
                () -> assertThat(savedSection.getId()).isEqualTo(1L),
                () -> assertThat(savedSection.getLine()).isEqualTo(_2호선),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(신림역),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(봉천역),
                () -> assertThat(savedSection.getDistance()).isEqualTo(new Distance(10))
        );
    }

    @Test
    void Section을_삭제한다() {
        // given
        insert_신림역_봉천역_2호선();
        Section section = new Section(1L, _2호선, 신림역, 봉천역, new Distance(10));
        sectionRepository.save(section);

        // when
        sectionRepository.delete(section);

        // then
        assertThat(sectionRepository.findAll().getSections()).hasSize(0);
    }

    @Test
    void 특정_Line에_존재하는_모든_Section을_반환한다() {
        // given
        insert_신림역_봉천역_2호선();
        Section section = new Section(1L, _2호선, 신림역, 봉천역, new Distance(10));
        sectionRepository.save(section);

        Line 신림선 = lineDao.insert(new Line("신림선", "청록색"));

        // when
        Sections sections_2호선 = sectionRepository.findByLine(_2호선);
        Sections sections_신림선 = sectionRepository.findByLine(신림선);

        // then
        assertAll(
                () -> assertThat(sections_2호선.getSections()).hasSize(1),
                () -> assertThat(sections_신림선.getSections()).hasSize(0)
        );
    }

    @Test
    void Section_정보들을_수정한다() {
        // given
        insert_신림역_봉천역_2호선();
        Station 서울대입구역 = stationDao.insert(new Station("서울대입구역"));
        Section section1 = new Section(1L, _2호선, 신림역, 봉천역, new Distance(10));
        Section section2 = new Section(2L, _2호선, 봉천역, 서울대입구역, new Distance(10));

        sectionRepository.saveSections(List.of(section1, section2));

        Sections _2호선_Sections = sectionRepository.findByLine(_2호선);
        _2호선_Sections.removeStation(서울대입구역);

        // when
        sectionRepository.update(_2호선.getId(), _2호선_Sections);

        // then
        assertThat(sectionRepository.findByLine(_2호선).getSections()).hasSize(1);
    }
}
