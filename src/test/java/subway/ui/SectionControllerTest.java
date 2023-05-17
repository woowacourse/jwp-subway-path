package subway.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

class SectionControllerTest {
    
    @Test
    @DisplayName("구역 추가 테스트")
    void addSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, "UP", 1);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 1L, 1);
        final SectionService sectionServiceMock = mock(SectionService.class);
        final SectionController sectionController = new SectionController(sectionServiceMock);
        // when
        when(sectionServiceMock.insertSection(sectionRequest)).thenReturn(List.of(sectionResponse));
        final ResponseEntity<List<SectionResponse>> listResponseEntity = sectionController.addSection(sectionRequest);
        // then
        Assertions.assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(listResponseEntity.getBody()).isEqualTo(List.of(sectionResponse));
    }
    
    @Test
    @DisplayName("구역 삭제 테스트")
    void deleteSection() {
        // given
        final SectionService sectionServiceMock = mock(SectionService.class);
        final SectionController sectionController = new SectionController(sectionServiceMock);
        // when
        final ResponseEntity<Void> voidResponseEntity = sectionController.deleteSection(null);
        // then
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    
    @Test
    @DisplayName("구역 조회 테스트")
    void getSections() {
        // given
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 1L, 1);
        final SectionService sectionServiceMock = mock(SectionService.class);
        final SectionController sectionController = new SectionController(sectionServiceMock);
        // when
        when(sectionServiceMock.findSectionsByLineId(1L)).thenReturn(List.of(sectionResponse));
        final ResponseEntity<List<SectionResponse>> listResponseEntity = sectionController.getSections(1L);
        // then
        Assertions.assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(listResponseEntity.getBody()).isEqualTo(List.of(sectionResponse));
    }
    
}