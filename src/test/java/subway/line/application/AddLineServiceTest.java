package subway.line.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.SaveLinePort;
import subway.line.domain.Line;
import subway.line.dto.AddLineRequest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AddLineServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private SaveLinePort saveLinePort;
    @InjectMocks
    private AddLineService addLineService;
    
    @Test
    void 노선을_추가한다() {
        // given
        final Line line1 = new Line("1호선", "파랑", 0L);
        final Line line2 = new Line("2호선", "초록", 0L);
        final Line line3 = new Line("3호선", "주황", 0L);
        given(getAllLinePort.getAll()).willReturn(new HashSet<>(Set.of(line1, line2)));
        given(saveLinePort.save(line3)).willReturn(3L);
        
        // when
        final Long LineId = addLineService.addLine(new AddLineRequest("3호선", "주황",  0L));
        
        // then
        assertThat(LineId).isEqualTo(3L);
    }
}
