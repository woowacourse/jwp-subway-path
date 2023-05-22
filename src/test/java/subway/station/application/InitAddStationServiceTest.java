package subway.station.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.section.application.port.output.SaveSectionPort;
import subway.station.application.port.output.SaveAllStationPort;
import subway.station.dto.InitAddStationRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class InitAddStationServiceTest {
    @Mock
    private SaveAllStationPort saveAllStationPort;
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetLineByIdPort getLineByIdPort;
    @Mock
    private SaveSectionPort saveSectionPort;
    
    @InjectMocks
    private InitAddStationService service;
    
    @Test
    void 최초_역_등록하기() {
        // given
        final Line line = new Line("1호선", "파랑", 0L);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getLineByIdPort.getLineById(anyLong())).willReturn(line);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> service.initAddStations(new InitAddStationRequest("잠실역", "선릉역", 3L, 1L)));
    }
    
    @Test
    void 최초_역_등록시_구간이_존재하면_예외_발생() {
        // given
        final Line line = new Line("1호선", "파랑", 0L);
        line.initAddStation("잠실역", "선릉역", 3L);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getLineByIdPort.getLineById(anyLong())).willReturn(line);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.initAddStations(new InitAddStationRequest("잠실역", "선릉역", 3L, 1L)));
    }
}
