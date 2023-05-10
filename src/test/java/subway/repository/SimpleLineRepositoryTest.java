package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.LineDirection;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.domain.dto.InsertionResult;

class SimpleLineRepositoryTest {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new SimpleLineRepository();
        stationRepository = new SimpleStationRepository();
    }

    @Test
    @DisplayName("노선을 등록한다.")
    void create() {
        //given
        Long upStationId = stationRepository.create(new Station("up"));
        Long downStationId = stationRepository.create(new Station("down"));
        Line line = Line.of("2호선", "초록색",
                List.of(new StationEdge(upStationId, 0), new StationEdge(downStationId, 5)));
        //when
        Long id = lineRepository.create(line);
        //then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("아이디로 노선을 찾는다.")
    void findById() {
        //given
        Long upStationId = stationRepository.create(new Station("up"));
        Long downStationId = stationRepository.create(new Station("down"));
        Line line = Line.of("2호선", "초록색",
                List.of(new StationEdge(upStationId, 0), new StationEdge(downStationId, 5)));
        Long id = lineRepository.create(line);
        //when
        Optional<Line> foundLine = lineRepository.findById(id);
        //then
        assertSoftly(softly -> {
            softly.assertThat(foundLine).isNotEmpty();
            softly.assertThat(foundLine.get().getId()).isEqualTo(id);
        });
    }

    @Test
    @DisplayName("추가한 edge를 반영한다.")
    void updateWithSavedEdge() {
        //given
        Long upStationId = stationRepository.create(new Station("up"));
        Long downStationId = stationRepository.create(new Station("down"));
        Line line = Line.of("2호선", "초록색",
                List.of(new StationEdge(upStationId, 0), new StationEdge(downStationId, 5)));
        Long id = lineRepository.create(line);
        Line createdLine = lineRepository.findById(id).get();
        int originalSize = createdLine.getStationEdges().size();

        Long middleStationId = stationRepository.create(new Station("middle"));
        InsertionResult insertionResult = createdLine.insertStation(middleStationId, downStationId, 2,
                LineDirection.UP);

        //when
        lineRepository.updateWithSavedEdge(createdLine, insertionResult.getInsertedEdge());

        //then
        Line actualLine = lineRepository.findById(id).get();
        assertThat(actualLine.getStationEdges()).hasSize(originalSize + 1);
    }

}