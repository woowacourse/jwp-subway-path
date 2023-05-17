package subway.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.AddStationRequest;
import subway.repository.SubwayRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @Mock
    private SubwayRepository subwayRepository;

    /**
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Test
    void 노선에_새로운_역을_추가할_수_있다() {
        Station newStation = Station.from("서울대입구");
        int distanceToUpstream = 3;
        AddStationRequest addStationRequest = new AddStationRequest(
                newStation.getName(),
                LINE_NUMBER_TWO.getName().getName(),
                JAMSIL_STATION.getName(),
                JAMSIL_NARU_STATION.getName(),
                distanceToUpstream
        );
        Line line = new Line(LINE_NUMBER_TWO);

        doReturn(line).when(subwayRepository).getLineByName(LINE_NUMBER_TWO.getName());
        doReturn(Optional.of(1L)).when(subwayRepository).findStationIdByName(newStation.getName());
        doReturn(new Stations(Set.of(JAMSIL_STATION, JAMSIL_NARU_STATION))).when(subwayRepository).getStations();

        SubwayService subwayService = new SubwayService(subwayRepository);

        subwayService.addStation(addStationRequest);

        assertThat(line.getSectionsWithoutEndPoints()).contains(
                new Section(JAMSIL_STATION, newStation, distanceToUpstream),
                new Section(newStation, JAMSIL_NARU_STATION, 2)
        );
    }
}
