package subway.application.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Line;
import subway.domain.repository.LineRepository;

class DeleteLineServiceTest {

    private LineRepository lineRepository;
    private DeleteLineService deleteLineService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        deleteLineService = new DeleteLineService(lineRepository);
    }

    @Test
    void deleteLine() {
        Long lineId = lineRepository.createLine(new Line("1호선"));
        deleteLineService.deleteLine(lineId);

        Assertions.assertThat(lineRepository.findAll()).hasSize(0);
    }
}