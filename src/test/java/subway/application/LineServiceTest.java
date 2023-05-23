package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.application.dto.LineDto;
import subway.domain.*;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineServiceTest extends ServiceTest {
    @Autowired
    private LineService lineService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("Line을 저장할 수 있다.")
    @Test
    void saveLineTest() {
        String lineName = "1호선";
        Long savedId = lineService.saveLine(lineName);

        LineDto findLineDto = lineService.findLineById(savedId);

        assertThat(findLineDto.getName()).isEqualTo(lineName);
    }

    @DisplayName("모든 Line의 정보를 가져올 수 있다.")
    @Test
    void findAllLinesDetailsTest() {
        //given
        String lineName = "1호선";
        Station 회기 = new Station("회기");
        Station 청량리 = new Station("청량리");
        Section section = new Section(회기, 청량리, new Distance(10));
        Sections sections = new Sections(List.of(section));

        Long lineId = lineService.saveLine(lineName);
        stationRepository.save(회기);
        stationRepository.save(청량리);
        sectionRepository.saveAll(lineId, sections);

        //when
        List<LineDto> allLines = lineService.findAllLinesDetails();

        //then
        assertThat(allLines).hasSize(1);
        assertThat(allLines.get(0).getName()).isEqualTo(lineName);
        assertThat(allLines.get(0).getStations()).extractingResultOf("getName")
                .containsExactlyInAnyOrder("회기", "청량리");
    }

}
