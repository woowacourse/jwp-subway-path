package subway.application.service.section;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.adapter.in.web.section.dto.SectionCreateRequest;
import subway.adapter.in.web.section.dto.SectionDeleteRequest;
import subway.application.port.out.line.LineQueryPort;
import subway.application.port.out.section.SectionCommandPort;
import subway.application.port.out.section.SectionQueryPort;
import subway.application.port.out.station.StationQueryPort;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

class SectionCommandServiceTest {
    private LineQueryPort lineQueryPort;
    private SectionCommandPort sectionCommandPort;
    private SectionQueryPort sectionQueryPort;
    private StationQueryPort stationQueryPort;
    private SectionCommandService sectionCommandService;

    @BeforeEach
    void setUp() {
        lineQueryPort = Mockito.mock(LineQueryPort.class);
        sectionCommandPort = Mockito.mock(SectionCommandPort.class);
        sectionQueryPort = Mockito.mock(SectionQueryPort.class);
        stationQueryPort = Mockito.mock(StationQueryPort.class);

        sectionCommandService = new SectionCommandService(lineQueryPort, sectionCommandPort, sectionQueryPort, stationQueryPort);
    }

    @Test
    @DisplayName("저장된 역으로 구간을 저장할 때 구간이 정상적으로 저장되는지 테스트")
    void createSection() {

        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineQueryPort.findLineById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선", 100)));
        given(stationQueryPort.findByName(new Station("허브신")))
                .willReturn(Optional.of(new Station("허브신")));
        given(stationQueryPort.findByName(new Station("나루토")))
                .willReturn(Optional.of(new Station("나루토")));

        given(sectionQueryPort.findAllByLineId(1L))
                .willReturn(sections);


        sectionCommandService.createSection(1L, new SectionCreateRequest("허브신", "나루토", 5L));
    }

    @Test
    @DisplayName("입력된 구간이 등록되지 않는 역이면 예외")
    void createSection_stationNotFoundException() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineQueryPort.findLineById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선", 10)));
        given(sectionQueryPort.findAllByLineId(1L))
                .willReturn(sections);


        Assertions.assertThatThrownBy(
                () -> sectionCommandService.createSection(1L, new SectionCreateRequest("허브신", "나루토", 5L))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("저장된 구간이 삭제가 정상적으로 되는지 테스트")
    void deleteStation() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineQueryPort.findLineById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선", 10)));
        given(stationQueryPort.findByName(new Station("비버")))
                .willReturn(Optional.of(new Station("비버")));
        given(sectionQueryPort.findAllByLineId(1L))
                .willReturn(sections);
        sectionCommandService.deleteStation(1L, new SectionDeleteRequest("비버"));
    }

    @Test
    @DisplayName("일치하는 역이 없으면 예외처리")
    void deleteStation_stationNotFoundException() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("아코"), 5L),
                new Section(1L, new Station("아코"), new Station("허브신"), 5L));

        given(lineQueryPort.findLineById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선", 10)));
        given(stationQueryPort.findByName(new Station("비버")))
                .willReturn(Optional.empty());
        given(sectionQueryPort.findAllByLineId(1L))
                .willReturn(sections);
        Assertions.assertThatThrownBy(() -> sectionCommandService.deleteStation(1L, new SectionDeleteRequest("비버")))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("일치하는 역이 없습니다.");
    }

    @Test
    @DisplayName("일치하는 노선이 없으면 예외처리")
    void deleteStation_lineNotFoundException() {
        given(lineQueryPort.findLineById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선", 1000)));
        given(stationQueryPort.findByName(new Station("비버")))
                .willReturn(Optional.of(new Station("비버")));
        given(sectionQueryPort.findAllByLineId(1L))
                .willReturn(Collections.emptyList());
        Assertions.assertThatThrownBy(() -> sectionCommandService.deleteStation(1L, new SectionDeleteRequest("비버")))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("노선의 역이 없습니다.");
    }

}