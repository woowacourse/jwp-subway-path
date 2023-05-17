package subway.application.section;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.SectionDeleteRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

class DetachStationServiceTest {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;
    private DetachStationService detachStationService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        stationRepository = Mockito.mock(StationRepository.class);
        sectionRepository = Mockito.mock(SectionRepository.class);

        detachStationService = new DetachStationService(lineRepository, stationRepository, sectionRepository);
    }

    @Test
    @DisplayName("저장된 구간이 삭제가 정상적으로 되는지 테스트")
    void deleteStation() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선")));
        given(stationRepository.findByName(new Station("비버")))
                .willReturn(Optional.of(new Station("비버")));
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(sections);
        detachStationService.deleteStation(1L, new SectionDeleteRequest("비버"));
    }

    @Test
    @DisplayName("일치하는 역이 없으면 예외처리")
    void deleteStation_stationNotFoundException() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("아코"), 5L),
                new Section(1L, new Station("아코"), new Station("허브신"), 5L));

        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선")));
        given(stationRepository.findByName(new Station("비버")))
                .willReturn(Optional.empty());
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(sections);
        Assertions.assertThatThrownBy(() -> detachStationService.deleteStation(1L, new SectionDeleteRequest("비버")))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("일치하는 역이 없습니다.");
    }

    @Test
    @DisplayName("일치하는 노선이 없으면 예외처리")
    void deleteStation_lineNotFoundException() {
        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line(1L, "1호선")));
        given(stationRepository.findByName(new Station("비버")))
                .willReturn(Optional.of(new Station("비버")));
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(Collections.emptyList());
        Assertions.assertThatThrownBy(() -> detachStationService.deleteStation(1L, new SectionDeleteRequest("비버")))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("노선의 역이 없습니다.");
    }
}