package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.domain.Line.UP_END_EDGE_DISTANCE;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.StationEdgeDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.domain.StationEdges;

@JdbcTest
class LineRepositoryTest {

    private static int INITIAL_DISTANCE = 5;

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineRepository = new DbLineRepository(
                new LineDao(jdbcTemplate),
                new StationEdgeDao(jdbcTemplate)
        );
        stationRepository = new DbStationRepository(
                new StationDao(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("노선을 등록한다.")
    void create() {
        //given
        Long upStationId = stationRepository.create(new Station("up"));
        Long downStationId = stationRepository.create(new Station("down"));
        StationEdges stationEdges = StationEdges.from(
                List.of(new StationEdge(upStationId, UP_END_EDGE_DISTANCE),
                        new StationEdge(downStationId, INITIAL_DISTANCE))
        );
        Line line = new Line("2호선", "초록색", stationEdges);
        //when
        Long id = lineRepository.create(line);
        //then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("아이디로 노선을 찾는다.")
    void findById() {
        //given
        Long id = createLineWithTwoStation().getId();
        //when
        Optional<Line> foundLine = lineRepository.findById(id);
        //then
        assertSoftly(softly -> {
            softly.assertThat(foundLine).isNotEmpty();
            softly.assertThat(foundLine.get().getId()).isEqualTo(id);
        });
    }

    private Line createLineWithTwoStation() {
        Long upStationId = stationRepository.create(new Station("up"));
        Long downStationId = stationRepository.create(new Station("down"));
        StationEdges stationEdges = StationEdges.from(
                List.of(new StationEdge(upStationId, UP_END_EDGE_DISTANCE),
                        new StationEdge(downStationId, INITIAL_DISTANCE))
        );
        Line line = new Line("2호선", "초록색", stationEdges);
        Long id = lineRepository.create(line);
        return lineRepository.findById(id).get();
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        Line line = createLineWithThreeStation();
        Long middleStationId = stationRepository.findByName("middle").get().getId();

        //when
        line.deleteStation(middleStationId);
        lineRepository.update(line);

        //then
        Line actualLine = lineRepository.findById(line.getId()).get();
        StationEdge seccondStationEdge = actualLine.getStationEdges().get(1);
        assertThat(seccondStationEdge.getDownStationId()).isNotEqualTo(middleStationId);
    }

    private Line createLineWithThreeStation() {
        Line createdLine = createLineWithTwoStation();

        Long middleStationId = stationRepository.create(new Station("middle"));
        Long downStationId = createdLine.getStationEdges().get(1).getDownStationId();
        createdLine.addStationUpperFrom(middleStationId, downStationId, Distance.from(2));
        lineRepository.update(createdLine);
        return createdLine;
    }

    @Test
    @DisplayName("노선정보를 제거한다.")
    void delete() {
        //given
        Line line = createLineWithTwoStation();

        //when
        lineRepository.deleteById(line.getId());

        //then
        assertThat(lineRepository.findById(line.getId())).isEmpty();
    }

    @Nested
    class UpdateLine {
        @Test
        @DisplayName("추가한 edge를 반영한다.")
        void updateWithInsertedEdge() {
            //given
            Line createdLine = createLineWithTwoStation();
            int originalSize = createdLine.getStationEdges().size();

            Long middleStationId = stationRepository.create(new Station("middle"));
            Long downStationId = createdLine.getStationEdges().get(1).getDownStationId();
            createdLine.addStationUpperFrom(middleStationId, downStationId, Distance.from(2));

            //when
            lineRepository.update(createdLine);

            //then
            Line actualLine = lineRepository.findById(createdLine.getId()).get();
            assertThat(actualLine.getStationEdges()).hasSize(originalSize + 1);
        }

        @Test
        @DisplayName("노선에서 역을 제거한다.")
        void deleteStation() {
            //given
            Line line = createLineWithThreeStation();
            Long middleStationId = stationRepository.findByName("middle").get().getId();

            //when
            line.deleteStation(middleStationId);
            lineRepository.update(line);

            //then
            Line actualLine = lineRepository.findById(line.getId()).get();
            StationEdge seccondStationEdge = actualLine.getStationEdges().get(1);
            assertThat(seccondStationEdge.getDownStationId()).isNotEqualTo(middleStationId);
        }
    }
}