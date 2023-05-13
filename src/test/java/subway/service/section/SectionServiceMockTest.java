package subway.service.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.subway.Line;
import subway.domain.subway.Sections;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.service.SectionService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static subway.fixture.SectionsFixture.createSections;

@ExtendWith(MockitoExtension.class)
public class SectionServiceMockTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LineRepository lineRepository;

    @Test
    @DisplayName("구간을 삽입한다.")
    void save_section_success() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "종합운동장역", "삼성역", 3);
        Sections sections = createSections();
        Line line = new Line(sections, 2, "2호선", "green");

        given(sectionRepository.findSectionsByLineName(req.getLineName())).willReturn(sections);
        given(lineRepository.findByLineNameAndSections(req.getLineName(), sections)).willReturn(line);

        // when
        sectionService.insertSection(req);

        // then
        verify(lineRepository).updateLine(sections, line.getLineNumber());
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void delete_section_success() {
        // given
        SectionDeleteRequest req = new SectionDeleteRequest(2, "종합운동장역");
        Sections sections = createSections();
        given(sectionRepository.findSectionsByLineNumber(req.getLineNumber())).willReturn(sections);

        // when
        sectionService.deleteSection(req);

        // then
        verify(lineRepository).updateLine(sections, req.getLineNumber());
    }
}
