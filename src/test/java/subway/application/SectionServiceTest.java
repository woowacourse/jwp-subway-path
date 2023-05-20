package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Section;
import subway.dto.StationToLineRequest;
import subway.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    private Long lineId;
    private StationToLineRequest request;

    @BeforeEach
    void init() {
        lineId = 1L;
        request = new StationToLineRequest(1L, 2L, 4);
    }

    @DisplayName("호선에 역이 존재하지 않으면 추가 쿼리만 한번 호출된다.")
    @Test
    void connectStation_success_isNothing() {
        //given
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(Collections.emptyList());
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getDownStationId()))
                .willReturn(Collections.emptyList());
        Section mockSection = Mockito.mock(Section.class);
        given(sectionRepository.insert(any()))
                .willReturn(mockSection);
        //when
        sectionService.connectStation(lineId, request);
        //then
        assertAll(
                () -> verify(sectionRepository, never()).update(any()),
                () -> verify(sectionRepository, times(1)).insert(any())
        );
    }

    @DisplayName("기존 역들 맨 앞에 넣는 경우 추가 쿼리만 한번 호출된다.")
    @Test
    void connectStation_success_addTop() {
        //given
        List<Section> sections = Mockito.mock(List.class);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(Collections.emptyList());
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getDownStationId()))
                .willReturn(sections);
        Section mockSection = Mockito.mock(Section.class);
        given(sectionRepository.insert(any()))
                .willReturn(mockSection);
        //when
        sectionService.connectStation(lineId, request);
        //then
        assertAll(
                () -> verify(sectionRepository, never()).update(any()),
                () -> verify(sectionRepository, times(1)).insert(any())
        );
    }

    @DisplayName("기존 역들 중간에 넣는 경우 업데이터 쿼리와 추가 쿼리가 한번씩 호출된다.")
    @Test
    void connectStation_success_addBetweenPosition() {
        //given
        List<Section> sections = Mockito.mock(List.class);
        when(sections.size()).thenReturn(2);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(sections);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getDownStationId()))
                .willReturn(Collections.emptyList());
        Section section = new Section(lineId, 1L, 3L, 11);
        when(sections.get(0)).thenReturn(section);
        Section mockSection = Mockito.mock(Section.class);
        given(sectionRepository.insert(any()))
                .willReturn(mockSection);
        //when
        sectionService.connectStation(lineId, request);
        //then
        assertAll(
                () -> verify(sectionRepository, times(1)).update(any()),
                () -> verify(sectionRepository, times(1)).insert(any())
        );
    }

    @DisplayName("기존 역들 마지막에 넣는 경우 추가 쿼리만 한번 호출된다.")
    @Test
    void connectStation_success_addEndPosition() {
        //given
        List<Section> sections = Mockito.mock(List.class);
        when(sections.size()).thenReturn(1);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(sections);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getDownStationId()))
                .willReturn(Collections.emptyList());
        Section mockSection = Mockito.mock(Section.class);
        given(sectionRepository.insert(any()))
                .willReturn(mockSection);
        //when
        sectionService.connectStation(lineId, request);
        //then
        assertAll(
                () -> verify(sectionRepository, never()).update(any()),
                () -> verify(sectionRepository, times(1)).insert(any())
        );
    }

    @DisplayName("거리가 양수가 아니면 예외가 발생한다.")
    @Test
    void connectStation_fail_no_positive_distance() {
        request = new StationToLineRequest(1L, 2L, 0);
        assertThatThrownBy(() -> sectionService.connectStation(lineId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는 양수여야합니다.");
    }

    @DisplayName("연결하려는 역이 이미 존재하면 예외가 발생한다.")
    @Test
    void connectStation_fail_exist() {
        List<Section> sections = Mockito.mock(List.class);
        when(sections.size()).thenReturn(1);
        given(sectionRepository.findSectionByLineIdAndStationId(any(), any()))
                .willReturn(sections);

        assertThatThrownBy(() -> sectionService.connectStation(lineId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 호선에 이미 역이 존재합니다.");
    }

    @DisplayName("노선에 역이 존재할 경우 요청하는 역 두개가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void connectStation_fail_isEmpty() {
        given(sectionRepository.findSectionByLineIdAndStationId(any(), any()))
                .willReturn(Collections.emptyList());
        given(sectionRepository.countByLineId(lineId))
                .willReturn(2);

        assertThatThrownBy(() -> sectionService.connectStation(lineId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선이 비었을 때를 제외하고 한 번에 두개의 역을 등록할 수 없습니다.");
    }

    @DisplayName("넣으려고 요청한 역의 거리가 기존 존재하는 거리보다 짧다면 예외가 발생한다.")
    @Test
    void connectStation_fail_invalid_distance() {
        List<Section> sections = Mockito.mock(List.class);
        when(sections.size()).thenReturn(2);

        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(sections);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getDownStationId()))
                .willReturn(Collections.emptyList());
        Section section = new Section(lineId, 1L, 3L, 1);
        when(sections.get(0)).thenReturn(section);

        assertThatThrownBy(() -> sectionService.connectStation(lineId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 역과의 거리가 요청한 거리보다 짧습니다.");
    }


    @DisplayName("호선에 역이 두개만 존재하면 모든 역을 삭제한다.")
    @Test
    void disconnectStation_success_deleteAll() {
        //given
        given(sectionRepository.countByLineId(lineId))
                .willReturn(2);
        //when
        sectionService.disconnectStation(lineId, request.getUpStationId());
        //then
        verify(sectionRepository, times(1)).deleteAllByLineId(lineId);
    }

    @DisplayName("삭제하려는 역이 중간에 있으면 업데이트 쿼리와 삭제 쿼를 호출한다.")
    @Test
    void disconnectStation_success_isInBetween() {
        //given
        given(sectionRepository.countByLineId(lineId))
                .willReturn(3);
        List<Section> sections = Mockito.mock(List.class);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(sections);
        when(sections.size()).thenReturn(2);
        Section frontSection = new Section(lineId, 3L, request.getUpStationId(), 4);
        Section backSection = new Section(lineId, request.getUpStationId(), request.getDownStationId(), 7);
        when(sections.get(0)).thenReturn(frontSection);
        when(sections.get(1)).thenReturn(backSection);

        //when
        sectionService.disconnectStation(lineId, request.getUpStationId());
        //then
        assertAll(
                () -> verify(sectionRepository, times(1)).deleteById(any()),
                () -> verify(sectionRepository, times(1)).update(any())
        );
    }

    @DisplayName("삭제하려는 역이 끝에 있으면 해당하는 역만 삭제한다.")
    @Test
    void disconnectStation_success_isEnd() {
        //given
        given(sectionRepository.countByLineId(lineId))
                .willReturn(3);
        List<Section> sections = Mockito.mock(List.class);
        given(sectionRepository.findSectionByLineIdAndStationId(lineId, request.getUpStationId()))
                .willReturn(sections);
        when(sections.size()).thenReturn(1);

        //when
        sectionService.disconnectStation(lineId, request.getUpStationId());

        //then
        verify(sectionRepository, times(1)).deleteByLineIdAndStationId(lineId, request.getUpStationId());
    }
}