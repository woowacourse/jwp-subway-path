package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.application.LineService;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationRepository;

import java.util.List;

@SpringBootTest
class LineServiceTest {
    @Autowired
    private LineService lineService;
    @MockBean
    private StationRepository stationRepository;
    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private SectionRepository sectionRepository;

    private Line 칠호선 = new Line(1L, "7호선", "green");
    private Line 분당선 = new Line(2L, "분당선", "yellow");
    private Station 반포역 = new Station(1L, "반포역");
    private Station 논현역 = new Station(2L, "논현역");
    private Station 학동역 = new Station(3L, "학동역");
    private Station 강남구청 = new Station(4L, "강남구청");
    private Station 청담역 = new Station(5L, "청담역");
    private Station 압구정로데오 = new Station(6L, "압구정로데오");
    private Station 서울숲 = new Station(7L, "서울숲");

    @BeforeEach
    void setup() {
        Mockito.when(lineRepository.findAll())
                .thenReturn(List.of(
                        칠호선,
                        분당선
                ));

        Mockito.when(sectionRepository.findAllByLineId(칠호선.getId()))
                .thenReturn(List.of(
                        new Section(1L, 반포역, 논현역, Distance.of(3)),
                        new Section(2L, 논현역, 학동역, Distance.of(4)),
                        new Section(3L, 학동역, 강남구청, Distance.of(3)),
                        new Section(4L, 강남구청, 청담역, Distance.of(4)),
                        new Section(5L, 청담역, new EmptyStation(), new EmptyDistance())
                ));

        Mockito.when(sectionRepository.findAllByLineId(분당선.getId()))
                .thenReturn(List.of(
                        new Section(6L, 강남구청, 압구정로데오, Distance.of(4)),
                        new Section(7L, 압구정로데오, 서울숲, Distance.of(5)),
                        new Section(8L, 서울숲, new EmptyStation(), new EmptyDistance())
                ));

        Mockito.when(stationRepository.findByName("반포역")).thenReturn(반포역);
        Mockito.when(stationRepository.findByName("청담역")).thenReturn(청담역);
        Mockito.when(stationRepository.findByName("논현역")).thenReturn(논현역);
        Mockito.when(stationRepository.findByName("서울숲")).thenReturn(서울숲);
    }
}