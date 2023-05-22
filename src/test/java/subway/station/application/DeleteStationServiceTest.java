package subway.station.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.section.application.port.output.DeleteSectionByLineIdPort;
import subway.section.application.port.output.SaveAllSectionPort;
import subway.section.domain.Section;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class DeleteStationServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetLineByIdPort getLineByIdPort;
    @Mock
    private GetStationByIdPort getStationByIdPort;
    @Mock
    private DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    @Mock
    private SaveAllSectionPort saveAllSectionPort;
    
    @InjectMocks
    private DeleteStationOnTheLineService deleteStationService;
    
    @Test
    void 해당_노선에서_역을_삭제한다() {
        // given
        final Set<Section> sections = new HashSet<>();
        sections.add(new Section("잠실역", "선릉역", 5L, "1호선"));
        final Line line = new Line("1호선", "파랑", sections);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getLineByIdPort.getLineById(anyLong())).willReturn(line);
        given(getStationByIdPort.getStationById(anyLong())).willReturn(new Station("잠실역"));
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> deleteStationService.deleteStationOnTheLine(1L, 1L));
    }
}
