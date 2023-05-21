package subway.station.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.domain.Line;
import subway.section.application.port.output.DeleteSectionByLinesPort;
import subway.section.application.port.output.SaveAllSectionsOfLinesPort;
import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.domain.Station;
import subway.station.dto.AddStationRequest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class DeleteStationOnAllLineServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetStationByIdPort getStationByIdPort;
    @Mock
    private DeleteSectionByLinesPort deleteSectionByLinesPort;
    @Mock
    private SaveAllSectionsOfLinesPort saveAllSectionsOfLinesPort;
    
    @InjectMocks
    private DeleteStationOnAllLineService deleteStationOnAllLineService;
    
    @Test
    void stationId로_모든_노선에_있는_해당_역을_삭제한다() {
        // given
        final Set<Section> sections = new HashSet<>();
        sections.add(new Section("잠실역", "선릉역", 5L, "1호선"));
        final Line line = new Line("1호선", "파랑", sections);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getStationByIdPort.getStationById(1L)).willReturn(new Station("잠실역"));
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> deleteStationOnAllLineService.deleteStationOnAllLine(1L));
    }
    
    @Test
    void 모든_노선에_존재하지_않는_역이면_예외_발생() {
        // given
        final Set<Section> sections = new HashSet<>();
        sections.add(new Section("잠실역", "선릉역", 5L, "1호선"));
        final Line line = new Line("1호선", "파랑", sections);
        given(getAllLinePort.getAll()).willReturn(Set.of(line));
        given(getStationByIdPort.getStationById(1L)).willReturn(new Station("청라역"));
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> deleteStationOnAllLineService.deleteStationOnAllLine(1L));
    }
}
