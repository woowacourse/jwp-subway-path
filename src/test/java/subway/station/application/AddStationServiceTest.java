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
import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.station.application.port.output.SaveStationPort;
import subway.station.dto.AddStationRequest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AddStationServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetLineByIdPort getLineByIdPort;
    @Mock
    private DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    @Mock
    private SaveAllSectionPort saveAllSectionPort;
    @Mock
    private SaveStationPort saveStationPort;
    
    @InjectMocks
    private AddStationService addStationService;
    
    @Test
    void 역을_등록한다() {
        // given
        final AddStationRequest request = new AddStationRequest(1L, "잠실역", Direction.RIGHT, "청라역", 3L);
        final Set<Section> sections = new HashSet<>();
        sections.add(new Section("잠실역", "선릉역", 5L, "1호선"));
        final Line line = new Line("1호선", "파랑", sections);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getLineByIdPort.getLineById(anyLong())).willReturn(line);
        given(saveStationPort.saveStation(any())).willReturn(1L);
        
        // when
        final Long stationId = addStationService.addStation(request);
        
        // then
        assertThat(stationId).isOne();
    }
}
