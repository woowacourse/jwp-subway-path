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
import subway.exception.IllegalDestinationException;
import subway.exception.NameLengthException;
import subway.exception.StationNotFoundException;
import subway.exception.StationsNotConnectedException;
import subway.repository.SubwayRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionFixture.*;
import static subway.utils.StationFixture.*;

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

        Station newStation = new Station(4L, "서울대입구");
        int distanceToUpstream = 3;
        Line line = new Line(LINE_NUMBER_TWO);

        @Test
        void 노선에_새로운_역을_추가할_수_있다() {
            AddStationRequest addStationRequest = new AddStationRequest(newStation.getName(), 1L, 2L, 3L, distanceToUpstream);
            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(line).when(subwayRepository).findLineById(1L);
            doReturn(SULLEUNG_JAMSIL_JAMSILNARU).when(subwayRepository).getStations();
            doReturn(4L).when(subwayRepository).addStation(new Station("서울대입구"));

            subwayService.addStation(addStationRequest);

            assertThat(line.getSectionsWithoutEndPoints()).contains(
                    new Section(JAMSIL_STATION, newStation, distanceToUpstream),
                    new Section(newStation, JAMSILNARU_STATION, 2)
            );
        }

        @Test
        void DB에_정상적으로_추가되지_않은_경우_예외를_던진다() {
            AddStationRequest addStationRequest = new AddStationRequest(newStation.getName(), 1L, 0L, 3L, distanceToUpstream);
            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(line).when(subwayRepository).findLineById(1L);
            doReturn(SULLEUNG_JAMSIL_JAMSILNARU).when(subwayRepository).getStations();

            assertThatThrownBy(() -> subwayService.addStation(addStationRequest))
                    .isInstanceOf(StationNotFoundException.class);
        }
    }

    /**
     * 저장되어 있는 정보
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Nested
    class delete_메서드는 {

        LineName lineName = new LineName("2호선");

        Line line = new Line(LINE_NUMBER_TWO);

        @Test
        void 역이_존재한다면_삭제할_수_있다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            long lineId = line.getId();

            DeleteStationRequest deleteStationRequest = new DeleteStationRequest(lineId, JAMSIL_STATION.getId());

            doReturn(line).when(subwayRepository).findLineById(lineId);
            doReturn(SULLEUNG_JAMSIL_JAMSILNARU).when(subwayRepository).getStations();

            assertThatNoException()
                    .isThrownBy(() -> subwayService.deleteStation(deleteStationRequest));
        }

        @Test
        void 삭제하려는_역이_없는_경우_예외를_던진다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            long lineId = line.getId();

            DeleteStationRequest deleteStationRequest = new DeleteStationRequest(line.getId(), 10L);

            doReturn(line).when(subwayRepository).findLineById(lineId);
            doReturn(SULLEUNG_JAMSIL_JAMSILNARU).when(subwayRepository).getStations();

            assertThatThrownBy(() -> subwayService.deleteStation(deleteStationRequest))
                    .isInstanceOf(StationNotFoundException.class)
                    .hasMessageContaining("찾는 역이 없습니다.");
        }
    }

    /**
     * 저장되어 있는 정보
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Nested
    class addNewLine_메서드는 {

        @Test
        void 새로운_노선을_추가할_수_있다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            Line line = createLine("야당", "홍대입구", 5, "경의중앙선");
            AddLineRequest addLineRequest = new AddLineRequest("경의중앙선", "야당", "홍대입구", 5);

            doReturn(SULLEUNG_JAMSIL_JAMSILNARU).when(subwayRepository).getStations();
            doReturn(new Lines(new ArrayList<>())).when(subwayRepository).getLines();

            assertThatNoException()
                    .isThrownBy(() -> subwayService.addNewLine(addLineRequest));
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

    /**
     * 저장되어 있는 정보
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Nested
    class findShortestPath_메서드는 {

        @Test
        void 출발역_또는_도착역이_존재하지_않으면_예외를_던진다() {
            SubwayPathRequest 도착역이_존재하지_않는_경우 = new SubwayPathRequest(1L, 0L);
            SubwayPathRequest 출발역이_존재하지_않는_경우 = new SubwayPathRequest(1L, 0L);

            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(new Stations(new HashSet<>())).when(subwayRepository).getStations();
            doReturn(new Sections(new ArrayList<>())).when(subwayRepository).getSections();
            doReturn(new Station("잠실역")).when(subwayRepository).findStationById(1L);
            doThrow(new StationNotFoundException("찾는 역이 존재하지 않습니다.")).when(subwayRepository).findStationById(0L);

            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> subwayService.findShortestPath(도착역이_존재하지_않는_경우))
                        .isInstanceOf(StationNotFoundException.class);
                softly.assertThatThrownBy(() -> subwayService.findShortestPath(출발역이_존재하지_않는_경우))
                        .isInstanceOf(StationNotFoundException.class);
            });
        }

        @Test
        void 출발역과_도착역이_연결되어있지_않은_경우_예외를_던진다() {
            // 역 추가
            Station 야당역 = new Station("야당역");
            Station 화전역 = new Station("화전역");
            Stations stations = new Stations(Set.of(SULLEUNG_STATION, JAMSIL_STATION, JAMSILNARU_STATION));
            stations.addNewStation(야당역);
            stations.addNewStation(화전역);
            long 야당_도착역_아이디 = subwayRepository.addStation(야당역);
            long 잠실_출발역_아이디 = 1L;

            // 구간 추가
            Section 화전역_to_야당역 = new Section(화전역, 야당역, 3);
            Sections sections = new Sections(new ArrayList<>(List.of(SULLEUNG_TO_JAMSIL, JAMSIL_TO_JAMSILNARU)));
            sections.addNewSection(화전역_to_야당역);

            SubwayService subwayService = new SubwayService(subwayRepository);

            doReturn(stations).when(subwayRepository).getStations();
            doReturn(sections).when(subwayRepository).getSections();
            doReturn(야당역).when(subwayRepository).findStationById(야당_도착역_아이디);
            doReturn(JAMSIL_STATION).when(subwayRepository).findStationById(잠실_출발역_아이디);

            SubwayPathRequest subwayPathRequest = new SubwayPathRequest(잠실_출발역_아이디, 야당_도착역_아이디);

            assertThatThrownBy(() -> subwayService.findShortestPath(subwayPathRequest))
                    .isInstanceOf(StationsNotConnectedException.class);
        }

        @Test
        void 출발역과_도착역이_같은_경우_예외를_던진다() {
            SubwayService subwayService = new SubwayService(subwayRepository);
            Stations stations = SULLEUNG_JAMSIL_JAMSILNARU;
            Sections sections = SULLEUNG_JAMSIL_JAMSILNARU_SECTIONS;
            SubwayPathRequest subwayPathRequest = new SubwayPathRequest(1L, 1L);

            doReturn(stations).when(subwayRepository).getStations();
            doReturn(sections).when(subwayRepository).getSections();
            doReturn(JAMSIL_STATION).when(subwayRepository).findStationById(1L);

            assertThatThrownBy(() -> subwayService.findShortestPath(subwayPathRequest))
                    .isInstanceOf(IllegalDestinationException.class)
                    .hasMessageContaining("출발역과 동일한 도착역을 입력할 수 없습니다.");
        }
    }
}
