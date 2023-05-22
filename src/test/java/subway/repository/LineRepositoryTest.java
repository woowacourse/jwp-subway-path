package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;
import subway.exception.LineNotFoundException;

@JdbcTest
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationDao stationDao;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationRepository = new StationRepository(stationDao);
        lineRepository = new LineRepository(lineDao, sectionDao, stationRepository);
    }

    @Test
    @DisplayName("노선 도메인을 저장한다.")
    void save() {
        Line line = new Line(null, "1호선", new Fare(100), null);

        Line savedLine = lineRepository.save(line);

        assertAll(
                () -> assertThat(savedLine.getId()).isNotNull(),
                () -> assertThat(savedLine.getName()).isEqualTo(line.getName())
        );
    }

    @Test
    @DisplayName("ID에 해당하는 노선 도메인을 찾아온다.")
    void findById() {
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "잠실역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "강남역"));
        Station station1 = stationRepository.findById(stationEntity1.getId());
        Station station2 = stationRepository.findById(stationEntity2.getId());
        Section section = new Section(null, station1, station2, new Distance(5));
        LinkedList<Section> sections = new LinkedList<>(List.of(section));

        Line line = new Line(null, "1호선", new Fare(100), new Sections(sections));
        Line savedLine = lineRepository.save(line);

        Line foundLine = lineRepository.findById(savedLine.getId());

        assertThat(foundLine).isEqualTo(savedLine);
    }

    @Test
    @DisplayName("ID에 해당하는 노선 도메인이 없으면 예외가 발생한다.")
    void findByIdFail() {
        Assertions.assertThatThrownBy(() -> lineRepository.findById(Long.MAX_VALUE))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("일치하는 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("모든 노선에 대한 모든 역을 가져온다.")
    void findAll() {
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "잠실역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "강남역"));
        StationEntity stationEntity3 = stationDao.insert(new StationEntity(null, "선릉역"));
        Station station1 = stationRepository.findById(stationEntity1.getId());
        Station station2 = stationRepository.findById(stationEntity2.getId());
        Station station3 = stationRepository.findById(stationEntity3.getId());
        Section section1 = new Section(null, station1, station2, new Distance(5));
        Section section2 = new Section(null, station3, station2, new Distance(10));
        LinkedList<Section> sections1 = new LinkedList<>(List.of(section1));
        LinkedList<Section> sections2 = new LinkedList<>(List.of(section2));
        Line line1 = new Line(null, "1호선", new Fare(100), new Sections(sections1));
        Line line2 = new Line(null, "2호선", new Fare(500), new Sections(sections2));
        Line savedLine1 = lineRepository.save(line1);
        Line savedLine2 = lineRepository.save(line2);

        List<Line> allLines = lineRepository.findAll();

        assertThat(allLines).containsExactly(savedLine1, savedLine2);
    }

    @Test
    @DisplayName("이름에 해당하는 노선이 있으면 true를 반환한다.")
    void existsNameTrue() {
        Line line = new Line(null, "1호선", new Fare(100), null);
        lineRepository.save(line);

        assertThat(lineRepository.existsByName("1호선")).isTrue();
    }

    @Test
    @DisplayName("이름에 해당하는 노선이 없으면 false를 반환한다.")
    void existsNameFalse() {
        Line line = new Line(null, "1호선", new Fare(100), null);
        lineRepository.save(line);

        assertThat(lineRepository.existsByName("2호선")).isFalse();
    }

    @Test
    @DisplayName("ID에 해당하는 노선을 삭제한다.")
    void deleteById() {
        Line line = new Line(null, "1호선", new Fare(100), null);
        Line savedLine = lineRepository.save(line);

        lineRepository.deleteById(savedLine.getId());

        assertThat(lineRepository.existsByName("1호선")).isFalse();
    }

    @Test
    @DisplayName("ID에 해당하는 노선에 구간을 추가한다.")
    void saveSection() {
        Line line = new Line(null, "1호선", new Fare(100), null);
        Line savedLine = lineRepository.save(line);
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "잠실역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "강남역"));

        lineRepository.saveSection(new SectionEntity(null, savedLine.getId(), stationEntity1.getId(),
                stationEntity2.getId(), 10));

        assertThat(lineRepository.findById(savedLine.getId()).getSections()).hasSize(1);
    }

    @Test
    @DisplayName("ID에 해당하는 노선에 구간을 삭제한다.")
    void deleteSection() {
        Line line = new Line(null, "1호선", new Fare(100), null);
        Line savedLine = lineRepository.save(line);
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "잠실역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "강남역"));
        SectionEntity savedSection = lineRepository.saveSection(
                new SectionEntity(null, savedLine.getId(), stationEntity1.getId(),
                        stationEntity2.getId(), 10));

        lineRepository.deleteSection(savedSection.getId());

        assertThat(lineRepository.findById(savedLine.getId()).getSections()).hasSize(0);
    }
}
