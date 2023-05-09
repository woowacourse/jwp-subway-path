package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionDao);
    }

    @DisplayName("section이 정상적으로 저장되고 sectionId 값을 반환한다.")
    @Test
    void save() {
        // given
        given(sectionDao.insert(any())).willReturn(1L);

        // when
        Long sectionId = sectionService.saveSection(new SectionRequest(2L, 1L, 1L, 3));

        // then
        assertThat(sectionId).isEqualTo(1L);
    }
}
