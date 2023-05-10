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


    private static int INITIAL_DISTANCE = 5;

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
                List.of(new StationEdge(upStationId, 0), new StationEdge(downStationId, INITIAL_DISTANCE)));
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
        Line line = Line.of("2호선", "초록색",
                List.of(new StationEdge(upStationId, 0), new StationEdge(downStationId, INITIAL_DISTANCE)));
        Long id = lineRepository.create(line);
        return lineRepository.findById(id).get();
    }

    @Test
    @DisplayName("추가한 edge를 반영한다.")
    void updateWithSavedEdge() {
        //given
        Line createdLine = createLineWithTwoStation();
        int originalSize = createdLine.getStationEdges().size();

        Long middleStationId = stationRepository.create(new Station("middle"));
        Long downStationId = createdLine.getStationEdges().get(1).getDownStationId();
        InsertionResult insertionResult = createdLine.insertStation(middleStationId, downStationId, 2,
                LineDirection.UP);

        //when
        lineRepository.updateWithSavedEdge(createdLine, insertionResult.getInsertedEdge());

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
        lineRepository.deleteStation(line, middleStationId);

        //then
        Line actualLine = lineRepository.findById(line.getId()).get();
        StationEdge seccondStationEdge = actualLine.getStationEdges().get(1);
        assertThat(seccondStationEdge.getDownStationId()).isNotEqualTo(middleStationId);
    }

    private Line createLineWithThreeStation() {
        Line createdLine = createLineWithTwoStation();

        Long middleStationId = stationRepository.create(new Station("middle"));
        Long downStationId = createdLine.getStationEdges().get(1).getDownStationId();
        InsertionResult insertionResult = createdLine.insertStation(middleStationId, downStationId, 2,
                LineDirection.UP);
        lineRepository.updateWithSavedEdge(createdLine, insertionResult.getInsertedEdge());
        return createdLine;
    }

}