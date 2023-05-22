package subway.application;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.AddLineRequest;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.SubwayPathRequest;
import subway.exception.LineNotFoundException;
import subway.exception.NameLengthException;
import subway.exception.StationNotFoundException;
import subway.repository.SubwayRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @Mock
    private SubwayRepository subwayRepository;

    /**
     * 저장되어 있는 정보
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Nested
    class addStation_메서드는 {
        Station newStation = new Station("서울대입구");
        int distanceToUpstream = 3;
        AddStationRequest addStationRequest = new AddStationRequest(
                newStation.getName(),
                LINE_NUMBER_TWO.getName().getName(),
                JAMSIL_STATION.getName(),
                JAMSIL_NARU_STATION.getName(),
                distanceToUpstream
        );
        Line line = new Line(LINE_NUMBER_TWO);

        @Test
        void 노선에_새로운_역을_추가할_수_있다() {
            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(line).when(subwayRepository).getLineByName(LINE_NUMBER_TWO.getName());
            doReturn(Optional.of(1L)).when(subwayRepository).findStationIdByName(newStation.getName());
            doReturn(new Stations(Set.of(JAMSIL_STATION, JAMSIL_NARU_STATION))).when(subwayRepository).getStations();

            subwayService.addStation(addStationRequest);

            assertThat(line.getSectionsWithoutEndPoints()).contains(
                    new Section(JAMSIL_STATION, newStation, distanceToUpstream),
                    new Section(newStation, JAMSIL_NARU_STATION, 2)
            );
        }

        @Test
        void DB에_정상적으로_추가되지_않은_경우_예외를_던진다() {
            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(line).when(subwayRepository).getLineByName(LINE_NUMBER_TWO.getName());
            doReturn(new Stations(Set.of(JAMSIL_STATION, JAMSIL_NARU_STATION))).when(subwayRepository).getStations();

            assertThatThrownBy(() -> subwayService.addStation(addStationRequest))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    class delete_메서드는 {

        String stationNameToDelete = "잠실";
        LineName lineName = new LineName("2호선");

        Line line = new Line(LINE_NUMBER_TWO);

        @Test
        void 역이_존재한다면_삭제할_수_있다() {
            SubwayService subwayService = new SubwayService(subwayRepository);

            DeleteStationRequest deleteStationRequest = new DeleteStationRequest(lineName.getName(), stationNameToDelete);

            doReturn(line).when(subwayRepository).getLineByName(new LineName(lineName.getName()));
            doReturn(new Stations(Set.of(new Station(stationNameToDelete)))).when(subwayRepository).getStations();

            assertThatNoException()
                    .isThrownBy(() -> subwayService.deleteStation(deleteStationRequest));
        }

        @Test
        void 삭제하려는_역이_없는_경우_예외를_던진다() {
            SubwayService subwayService = new SubwayService(subwayRepository);

            DeleteStationRequest deleteStationRequest = new DeleteStationRequest(lineName.getName(), "존재하지 않는 역");

            doReturn(line).when(subwayRepository).getLineByName(lineName);
            doReturn(new Stations(new HashSet<>())).when(subwayRepository).getStations();

            assertThatThrownBy(() -> subwayService.deleteStation(deleteStationRequest))
                    .isInstanceOf(StationNotFoundException.class)
                    .hasMessageContaining("삭제하고자 하는 역이 존재하지 않습니다.");
        }
    }

    @Nested
    class addNewLine_메서드는 {

        @Test
        void 새로운_노선을_추가할_수_있다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            Line line = createLine("야당", "홍대입구", 5, "경의중앙선");
            AddLineRequest addLineRequest = new AddLineRequest("경의중앙선", "야당", "홍대입구", 5);

            doReturn(new Stations(Set.of())).when(subwayRepository).getStations();
            doReturn(new Lines(new ArrayList<>())).when(subwayRepository).getLines();
            doReturn(1L).when(subwayRepository).addNewLine(line);

            subwayService.addNewLine(addLineRequest);

            assertThat(subwayService.findAllLines().toString())
                    .contains("경의중앙선");
        }

        @Test
        void 잘못된_노선이름을_입력하면_예외를_던지고_저장되지_않는다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            AddLineRequest addLineRequest = new AddLineRequest("", "야당", "홍대입구", 5);

            doReturn(new Lines(new ArrayList<>())).when(subwayRepository).getLines();

            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> subwayService.addNewLine(addLineRequest))
                        .isInstanceOf(NameLengthException.class);
                softly.assertThat(subwayService.findAllLines().toString())
                        .doesNotContain("야당", "홍대입구", "경의중앙선");
            });
        }

        private Line createLine(String upStreamName, String downStreamName, int distance, String lineNameToAdd) {
            Station upstreamToAdd = new Station(upStreamName);
            Station downstreamToAdd = new Station(downStreamName);
            Section section = new Section(upstreamToAdd, downstreamToAdd, distance);
            LineName lineName = new LineName(lineNameToAdd);
            return new Line(lineName, section);
        }
    }

    @Test
    void 잘못된_line_아이디를_입력하면_예외를_던진다() {
        SubwayService subwayService = new SubwayService(subwayRepository);

        doReturn(Optional.empty()).when(subwayRepository).getLineById(0L);

        assertThatThrownBy(() -> subwayService.findLineById(0L))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("조회하고자 하는 노선이 없습니다");
    }

    @Nested
    class findShortestPath메서드는 {

        @Test
        void 출발역_또는_도착역이_존재하지_않으면_예외를_던진다() {
            SubwayPathRequest 도착역이_존재하지_않는_경우 = new SubwayPathRequest(1L, 0L);
            SubwayPathRequest 출발역이_존재하지_않는_경우 = new SubwayPathRequest(1L, 0L);

            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(new Stations(new HashSet<>())).when(subwayRepository).getStations();
            doReturn(new Sections(new ArrayList<>())).when(subwayRepository).getSections();
            doReturn(new Station("잠실역")).when(subwayRepository).findStation(1L);
            doThrow(new StationNotFoundException("찾는 역이 존재하지 않습니다.")).when(subwayRepository).findStation(0L);

            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> subwayService.findShortestPath(도착역이_존재하지_않는_경우))
                        .isInstanceOf(StationNotFoundException.class);
                softly.assertThatThrownBy(() -> subwayService.findShortestPath(출발역이_존재하지_않는_경우))
                        .isInstanceOf(StationNotFoundException.class);
            });
        }
    }
}
