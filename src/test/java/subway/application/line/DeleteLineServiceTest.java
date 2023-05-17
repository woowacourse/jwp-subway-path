package subway.application.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Line;

class DeleteLineServiceTest {

    private LineRepository lineRepository;
    private DeleteLineService deleteLineService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        deleteLineService = new DeleteLineService(lineRepository);
    }

    @Test
    @DisplayName("저장된 노선이 정상적으로 삭제되는지 테스트")
    void deleteLine() {
        Long lineId = lineRepository.createLine(new Line("1호선"));
        deleteLineService.deleteLine(lineId);

        Assertions.assertThat(lineRepository.findAll()).hasSize(0);
    }
}