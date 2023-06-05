package subway.line.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.interstation.domain.InterStation;
import subway.interstation.domain.InterStations;
import subway.line.domain.Line;
import subway.line.out.FakeLineRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선 관련 기능 테스트")
class LineDeleteServiceTest {

    private FakeLineRepository fakeLineRepository;
    private LineDeleteService lineDeleteService;

    @BeforeEach
    void setUp() {
        fakeLineRepository = new FakeLineRepository();
        lineDeleteService = new LineDeleteService(fakeLineRepository);
    }

    @Test
    void 정상적으로_지워진다() {
        // given
        Line savedLine = fakeLineRepository.save(new Line(1L, "2호선", "green", new InterStations(
                List.of(
                        new InterStation(1L, 1L, 2L, 3),
                        new InterStation(2L, 2L, 3L, 3)
                )
        )));

        // when
        lineDeleteService.deleteLineById(savedLine.getId());

        // then
        assertThat(fakeLineRepository.findAll()).isEmpty();
    }
}
