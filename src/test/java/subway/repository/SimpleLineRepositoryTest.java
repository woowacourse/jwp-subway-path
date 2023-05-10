package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationEdge;

class SimpleLineRepositoryTest {

    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new SimpleLineRepository();
    }

    @Test
    @DisplayName("노선을 등록한다.")
    void create() {
        //given
        Line line = Line.of("2호선", "초록색", new StationEdge(new Station("잠실"), new Station("건대"), 7));
        //when
        Long id = lineRepository.create(line);
        //then
        assertThat(id).isNotNull();
    }

}