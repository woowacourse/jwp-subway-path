package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.*;
import subway.dto.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private final Station STATION1 = new Station(1L, "잠실새내");
    private final Station STATION2 = new Station(2L, "잠실");
    private final Station STATION3 = new Station(3L, "잠실나루");
    private final Station STATION4 = new Station(4L, "몽촌토성");
    private final Station STATION5 = new Station(5L, "석촌");
    private final Distance DISTANCE1 = new Distance(10);
    private final Distance DISTANCE2 = new Distance(15);
    private final Section SECTION1 = new Section(1L,STATION1, STATION2, DISTANCE1);
    private final Section SECTION2 = new Section(2L,STATION2, STATION3, DISTANCE2);
    private final Section SECTION3 = new Section(3L,STATION4, STATION3, DISTANCE1);
    private final Section SECTION4 = new Section(4L,STATION3, STATION5, DISTANCE2);
    private final List<Section> SECTION_LIST1 = List.of(SECTION1, SECTION2);
    private final List<Section> SECTION_LIST2 = List.of(SECTION3, SECTION4);
    private final Sections SECTIONS1 = new Sections(SECTION_LIST1);
    private final Sections SECTIONS2 = new Sections(SECTION_LIST2);
    private final Line LINE1 = new Line(1L, new LineName("2호선"), new LineColor("초록"), SECTIONS1);
    private final Line LINE2 = new Line(2L, new LineName("8호선"), new LineColor("파랑"), SECTIONS2);
    private final List<Line> LINES = List.of(LINE1, LINE2);

    @Mock
    StationRepository stationRepository;
    @Mock
    LineRepository lineRepository;
    @InjectMocks
    PathService pathService;


    @DisplayName("출발역과 도착역에 대한 요청을 받아서 거리, 요금, 경로에 대한 값을 반환한다")
    @Test
    void 출발_도착_요청을_받아_거리_요금_경로를_반환한다() {
        //given
        PathRequest pathRequest = new PathRequest(1L, 5L);
        when(stationRepository.findById(1L)).thenReturn(STATION1);
        when(stationRepository.findById(5L)).thenReturn(STATION5);
        when(lineRepository.findAll()).thenReturn(LINES);
        //when
        PathResponse pathResponse = pathService.calculatePath(pathRequest);
        //then
        ;
        Assertions.assertThat(pathResponse).usingRecursiveComparison()
                .isEqualTo(new PathResponse(
                        40,
                        1850,
                        List.of(
                                new SectionResponse(
                                        new StationResponse(1L, "잠실새내"),
                                        new StationResponse(2L, "잠실"),
                                        new LineResponse(1L, "2호선", "초록"),
                                        10
                                ),
                                new SectionResponse(
                                        new StationResponse(2L, "잠실"),
                                        new StationResponse(3L, "잠실나루"),
                                        new LineResponse(1L, "2호선", "초록"),
                                        15
                                ),
                                new SectionResponse(
                                        new StationResponse(3L, "잠실나루"),
                                        new StationResponse(5L, "석촌"),
                                        new LineResponse(2L, "8호선", "파랑"),
                                        15
                                )
                        )
                ));
    }
}
