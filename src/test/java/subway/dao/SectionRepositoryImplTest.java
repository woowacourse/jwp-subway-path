package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.DB_DELETE_ERROR;
import static subway.fixture.SectionFixture.구간_저장_요청;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.exception.GlobalException;

@ExtendWith(MockitoExtension.class)
class SectionRepositoryImplTest {

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private SectionRepositoryImpl sectionRepository;

    @Test
    @DisplayName("특정 노선의 구간 정보를 저장한다.")
    void insert() {
        // given
        when(sectionDao.insert(any()))
            .thenReturn(1L);

        // when
        final Long savedId = sectionRepository.insert(구간_저장_요청);

        // then
        assertThat(savedId)
            .isSameAs(1L);
    }

    @Test
    @DisplayName("이전의 구간 정보를 제거한다.")
    void deleteOldSection_success() {
        // given
        when(sectionDao.deleteByLineIdAndSourceStationId(any(), any()))
            .thenReturn(1);

        // expected
        assertDoesNotThrow(() -> sectionRepository.deleteOldSection(1L, 1L));
    }

    @ParameterizedTest(name = "이전의 구간 정보를 제거 시 제거된 행 개수가 1이 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void deleteOldSection_fail(final int deletedCount) {
        // given
        when(sectionDao.deleteByLineIdAndSourceStationId(any(), any()))
            .thenReturn(deletedCount);

        // expected
        assertThatThrownBy(() -> sectionRepository.deleteOldSection(1L, 1L))
            .isInstanceOf(GlobalException.class)
            .extracting("errorCode")
            .isEqualTo(DB_DELETE_ERROR);
    }

    @Test
    @DisplayName("노선 아이디와 역 아이디가 주어지면 일치하는 구간을 제거한다.")
    void deleteByLineIdAndStationId() {
        // given
        when(sectionDao.deleteByLineIdAndStationId(anyLong(), anyLong()))
            .thenReturn(2);

        // expected
        assertDoesNotThrow(() -> sectionRepository.deleteByLineIdAndStationId(1L, 1L));
    }
}
