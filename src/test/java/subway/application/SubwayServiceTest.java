package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.PathResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {
    @Mock
    private SectionService sectionService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private SubwayService subwayService;

    @DisplayName("최단 경로를 찾는다.")
    @Test
    void findPathBetween() {
        //given
        final Station 수성 = new Station(1L, "수성");
        final Station 금성 = new Station(2L, "금성");
        final Station 지구 = new Station(3L, "지구");
        final Station 화성 = new Station(4L, "화성");
        final Station 잠실 = new Station(5L, "잠실");
        given(sectionService.findAll())
                .willReturn(List.of(
                                new Section(수성, 금성, new Distance(10)),
                                new Section(금성, 지구, new Distance(20)),
                                new Section(지구, 화성, new Distance(30)),
                                new Section(지구, 잠실, new Distance(40))
                        )
                );
        given(stationService.findById(anyLong()))
                .willReturn(수성, 잠실);

        //when
        final PathResponse pathResponse = subwayService.findPathBetween(1L, 5L);
        final int pare = 1250 + 800 + 300;

        //then
        assertAll(
                () -> assertThat(pathResponse.getPaths()).containsExactly(
                        StationResponse.of(수성),
                        StationResponse.of(금성),
                        StationResponse.of(지구),
                        StationResponse.of(잠실)
                ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(70),
                () -> assertThat(pathResponse.getFare()).isEqualTo(pare)
        );
    }

}
