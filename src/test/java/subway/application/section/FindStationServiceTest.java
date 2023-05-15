package subway.application.section;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.ui.dto.response.StationResponse;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

class FindStationServiceTest {

    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private FindStationService findStationService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        sectionRepository = Mockito.mock(SectionRepository.class);

        findStationService = new FindStationService(lineRepository, sectionRepository);
    }

    @Test
    @DisplayName("입력된 노선이 없으면 예외처리")
    void findAllByLine() {
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(Collections.emptyList());

        Assertions.assertThatThrownBy(() -> findStationService.findAllByLine(1L))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("노선의 역이 없습니다.");
    }

    @Test
    @DisplayName("정렬된 역 출력")
    void sortLine() {
        List<Section> sections = List.of(
                new Section(1L, new Station("라빈"), new Station("비버"), 5L),
                new Section(1L, new Station("허브신"), new Station("루터회관"), 5L),
                new Section(1L, new Station("비버"), new Station("허브신"), 5L));
        given(sectionRepository.findAllByLineId(1L))
                .willReturn(sections);


        List<StationResponse> stationResponses = findStationService.findAllByLine(1L);
        List<Station> result = List.of(new Station("라빈"), new Station("비버"), new Station("허브신"), new Station("루터회관"));

        assertAll(
                () -> Assertions.assertThat(stationResponses).usingRecursiveComparison().isEqualTo(result)
        );
    }
}