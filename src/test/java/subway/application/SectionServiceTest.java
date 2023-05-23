package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.application.dto.SectionDto;
import subway.domain.*;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

class SectionServiceTest extends ServiceTest{
    @Autowired
    private SectionService sectionService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @DisplayName("구간을 저장할 수 있다.")
    @Test
    void saveSectionTest() {
        //given
        Long lineId = lineRepository.save(new Line("1호선"));
        SectionDto sectionDto1 = new SectionDto("회기", "청량리", 10);
        SectionDto sectionDto2 = new SectionDto("청량리", "용산", 10);

        //when
        sectionService.saveSection(lineId, sectionDto1);
        sectionService.saveSection(lineId, sectionDto2);

        //then
        Sections sections = sectionRepository.findAllByLineId(lineId);
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getSections()).extractingResultOf("getStartStation")
                .extractingResultOf("getName")
                .containsExactly("회기", "청량리");
    }

    @DisplayName("구간을 삭제할 수 있다.")
    @Test
    void deleteSectionTest() {
        //given
        Long lineId = lineRepository.save(new Line("1호선"));
        SectionDto sectionDto1 = new SectionDto("회기", "청량리", 5);
        SectionDto sectionDto2 = new SectionDto("청량리", "용산", 5);
        sectionService.saveSection(lineId, sectionDto1);
        sectionService.saveSection(lineId, sectionDto2);

        //when
        Long 청량리Id = stationRepository.findIdByName("청량리");
        assertThat(sectionRepository.findAllByLineId(lineId).getSections()).hasSize(2);
        sectionService.deleteSection(lineId, 청량리Id);

        //then
        Sections sections = sectionRepository.findAllByLineId(lineId);
        assertThat(sections.getSections()).extractingResultOf("getStartStation")
                .extractingResultOf("getName")
                .containsExactly("회기");
        assertThat(sections.getSections()).extractingResultOf("getEndStation")
                .extractingResultOf("getName")
                .containsExactly("용산");
    }
}
