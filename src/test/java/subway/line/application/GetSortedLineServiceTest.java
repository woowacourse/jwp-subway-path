package subway.line.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.dto.GetSortedLineResponse;
import subway.section.domain.Direction;
import subway.section.domain.Section;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class GetSortedLineServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @Mock
    private GetLineByIdPort getLineByIdPort;
    
    @InjectMocks
    private GetSortedLineService getSortedLineService;
    
    @Test
    void 해당_노선에서_정렬된_역들을_가져온다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = new HashSet<>(Set.of(firstSection, secondSection, thirdSection));
        final Line line1 = new Line("1호선", "파랑", initSections);
        final Line line2 = new Line("2호선", "초록", 0L);
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        line1.addStation(third, Direction.LEFT, additionalStation, additionalDistance);
        given(getAllLinePort.getAll()).willReturn(new HashSet<>(Set.of(line1, line2)));
        given(getLineByIdPort.getLineById(1L)).willReturn(line1);
        
        // when
        final GetSortedLineResponse response = getSortedLineService.getSortedLine(1L);
        
        // then
        assertThat(response.getSortedStations()).containsExactly("잠실역", "가양역", "화정역", "종합운동장", "선릉역");
    }
}
