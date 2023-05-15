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
import subway.ui.dto.request.SectionCreateRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

class AttachStationServiceTest {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;
    private AttachStationService attachStationService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        stationRepository = Mockito.mock(StationRepository.class);
        sectionRepository = Mockito.mock(SectionRepository.class);

        attachStationService = new AttachStationService(lineRepository, stationRepository, sectionRepository);
    }

    @Test
    @DisplayName("구간이 잘 저장되는지 테스트")
    void createSection1() {

        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineRepository.findById(1l))
                .willReturn(new Line(1L, "1호선"));
        given(stationRepository.findByName(new Station("허브신")))
                .willReturn(Optional.of(new Station("허브신")));
        given(stationRepository.findByName(new Station("나루토")))
                .willReturn(Optional.of(new Station("나루토")));

        given(sectionRepository.findAllByLineId(1L))
                .willReturn(sections);


        attachStationService.createSection(1L, new SectionCreateRequest("허브신", "나루토", 5L));
    }

    @Test
    @DisplayName("입력된 구간이 등록되지 않는 역이면 예외")
    void createSection_stationNotFoundException() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));

        given(lineRepository.findById(1l))
                .willReturn(new Line(1L, "1호선"));
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(sections);


        Assertions.assertThatThrownBy(
                () -> attachStationService.createSection(1L, new SectionCreateRequest("허브신", "나루토", 5L))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 역입니다.");
    }
}