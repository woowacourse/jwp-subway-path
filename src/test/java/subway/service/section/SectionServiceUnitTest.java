package subway.service.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.domain.subway.Line;
import subway.domain.subway.Sections;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.event.RouteUpdateEvent;
import subway.exception.SectionDuplicatedException;
import subway.exception.SectionForkedException;
import subway.exception.SectionNotConnectException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.service.SectionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static subway.fixture.SectionsFixture.createSections;

@ExtendWith(MockitoExtension.class)
public class SectionServiceUnitTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    @DisplayName("구간을 삽입한다.")
    void save_section_success() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "종합운동장역", "삼성역", 3);
        Sections sections = createSections();
        Line line = new Line(sections, 2, "2호선", "green");

        given(sectionRepository.findSectionsByLineName(req.getLineName())).willReturn(sections);
        given(lineRepository.findByLineNameWithSections(req.getLineName(), sections)).willReturn(line);

        // when
        sectionService.insertSection(req);

        // then
        verify(lineRepository).insertSectionInLine(sections, line.getLineNumber());
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }

    @Test
    @DisplayName("중복된 역을 넣으면 예외를 발생시킨다.")
    void throws_exception_when_save_duplicated_section() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        Sections sections = createSections();
        Line line = new Line(sections, 2, "2호선", "green");

        given(sectionRepository.findSectionsByLineName(req.getLineName())).willReturn(sections);
        given(lineRepository.findByLineNameWithSections(req.getLineName(), sections)).willReturn(line);

        // when & then
        assertThatThrownBy(() -> sectionService.insertSection(req))
                .isInstanceOf(SectionDuplicatedException.class);
    }

    @Test
    @DisplayName("연결할 수 없는 구간을 넣으면 예외를 발생시킨다.")
    void throws_exception_when_save_invalid_section() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "판교역", "정자역", 3);
        Sections sections = createSections();
        Line line = new Line(sections, 2, "2호선", "green");

        given(sectionRepository.findSectionsByLineName(req.getLineName())).willReturn(sections);
        given(lineRepository.findByLineNameWithSections(req.getLineName(), sections)).willReturn(line);

        // when & then
        assertThatThrownBy(() -> sectionService.insertSection(req))
                .isInstanceOf(SectionNotConnectException.class);
    }

    @Test
    @DisplayName("갈래길이 생기면 예외를 발생시킨다.")
    void throws_exception_when_new_section_is_forked() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "잠실역", "선릉역", 10);
        Sections sections = createSections();
        Line line = new Line(sections, 2, "2호선", "green");

        given(sectionRepository.findSectionsByLineName(req.getLineName())).willReturn(sections);
        given(lineRepository.findByLineNameWithSections(req.getLineName(), sections)).willReturn(line);

        // when & then
        assertThatThrownBy(() -> sectionService.insertSection(req))
                .isInstanceOf(SectionForkedException.class);
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
        verify(lineRepository).insertSectionInLine(sections, req.getLineNumber());
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }
}
