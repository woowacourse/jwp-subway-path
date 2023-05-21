package subway.line.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.DeleteLinePort;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.section.application.port.output.DeleteSectionByLineIdPort;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class DeleteLineServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetLineByIdPort getLineByIdPort;
    @Mock
    private DeleteLinePort deleteLinePort;
    @Mock
    private DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    
    @InjectMocks
    private DeleteLineService deleteLineService;
    
    @Test
    void lineId로_노선_삭제() {
        // given
        final Line line1 = new Line("1호선", "파랑", 0L);
        final Line line2 = new Line("2호선", "초록", 0L);
        given(getAllLinePort.getAll()).willReturn(new HashSet<>(Set.of(line1, line2)));
        given(getLineByIdPort.getLineById(anyLong())).willReturn(line1);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> deleteLineService.deleteLine(1L));
    }
}
