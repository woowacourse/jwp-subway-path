package subway.shortestpathfinder.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.domain.Line;
import subway.shortestpathfinder.dto.GetShortestPathResponse;
import subway.section.domain.Section;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static subway.shortestpathfinder.domain.AgeGroupFeeCalculator.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class GetShortestPathServiceTest {
    @Mock
    private GetAllLinePort getAllLinePort;
    @InjectMocks
    private GetShortestPathService service;
    
    @BeforeEach
    void setUp() {
        service = new GetShortestPathService(getAllLinePort);
    }
    
    @Test
    void 최단_경로를_조회한다() {
        // given
        String first = "잠실역";
        String second = "가양역";
        String third = "화정역";
        String fourth = "종합운동장";
        String fifth = "선릉역";
        
        long distance1 = 2L;
        long distance2 = 3L;
        long distance3 = 6L;
        long distance4 = 7L;
        Section firstSection = new Section(first, second, distance1, "1호선");
        Section secondSection = new Section(second, third, distance2, "1호선");
        Section thirdSection = new Section(third, fourth, distance3, "1호선");
        Section fourthSection = new Section(fourth, fifth, distance4, "1호선");
        
        Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line1 = new Line("1호선", "파랑", initSections);
        
        first = "청라역";
        second = "검암역";
        third = "화정역";
        fourth = "마곡나루역";
        fifth = "김포공항역";
        
        distance1 = 5L;
        distance2 = 6L;
        distance3 = 7L;
        distance4 = 8L;
        firstSection = new Section(first, second, distance1, "2호선");
        secondSection = new Section(second, third, distance2, "2호선");
        thirdSection = new Section(third, fourth, distance3, "2호선");
        fourthSection = new Section(fourth, fifth, distance4, "2호선");
        
        initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line2 = new Line("2호선", "초록", initSections);
        given(getAllLinePort.getAll()).willReturn(Set.of(line1, line2));
        
        // when
        final GetShortestPathResponse response = service.getShortestPath("김포공항역", "선릉역", ADULT);
        
        // then
        assertAll(
                () -> assertThat(response.getShortestPath()).containsExactly("김포공항역", "마곡나루역", "화정역", "종합운동장", "선릉역"),
                () -> assertThat(response.getShortestDistance()).isEqualTo(28),
                () -> assertThat(response.getFee()).isEqualTo(1650)
        );
    }
}
