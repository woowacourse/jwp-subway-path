package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionRepository, stationRepository, lineRepository);
    }

    @Test
    @DisplayName("Section이 정상적으로 저장된다.")
    void save() {
        // given
        final Station station1 = new Station(1L, "반월당역");
        final Station station2 = new Station(2L, "신천역");
        final Section section = new Section(station1, station2, 3);
        final Sections sections = new Sections(new ArrayList<>());
        sections.addSection(section);
        given(stationRepository.findById(anyLong())).willReturn(station1);
        given(sectionRepository.findByLineId(anyLong())).willReturn(sections);
        willDoNothing().given(sectionRepository).insertAll(anyLong(), any());

        // when & then
        assertDoesNotThrow(() -> sectionService.save(
                new SectionRequest(1L, 1L, 2L, 3))
        );
    }

    @Test
    @DisplayName("Section이 정상적으로 삭제된다.")
    void delete() {
        // given
        final Station station1 = new Station(1L, "반월당역");
        final Station station2 = new Station(2L, "신천역");
        final Section section = new Section(station1, station2, 3);
        final Sections sections = new Sections(new ArrayList<>());
        sections.addSection(section);
        given(stationRepository.findById(anyLong())).willReturn(station1);
        given(sectionRepository.findByLineId(anyLong())).willReturn(sections);
        willDoNothing().given(sectionRepository).deleteAllByLineId(anyLong());

        // when & then
        assertDoesNotThrow(() -> sectionService.delete(1L, 1L));
    }

    @Test
    @DisplayName("lineId로 Section이 정상적으로 조회된다.")
    void findByLineId() {
        // given
        final Station station1 = new Station(1L, "반월당역");
        final Station station2 = new Station(2L, "신천역");
        final Section section = new Section(station1, station2, 3);
        given(sectionRepository.findByLineId(anyLong())).willReturn(new Sections(List.of(section)));

        // when
        final List<SectionResponse> sectionResponses = sectionService.findByLineId(1L);

        // then
        assertThat(sectionResponses.size()).isEqualTo(1);
        assertThat(sectionResponses.get(0).getUpStationId()).isEqualTo(section.getUpStation().getId());
        assertThat(sectionResponses.get(0).getDownStationId()).isEqualTo(section.getDownStation().getId());
        assertThat(sectionResponses.get(0).getDistance()).isEqualTo(section.getDistance());
    }
}
