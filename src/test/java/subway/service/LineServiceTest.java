package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.LineCreateRequest;
import subway.dao.LineDao;
import subway.entity.LineEntity;
import subway.exception.InvalidLineNameException;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @Nested
    @DisplayName("노선 추가시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 정보라면 노선을 추가한다.")
        void createLine() {
            final LineEntity entity = new LineEntity(1L, "2호선", "초록색");
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색");
            given(lineDao.save(any(LineEntity.class))).willReturn(entity);

            final Long lineId = lineService.createLine(request);

            assertThat(lineId).isEqualTo(1L);
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void createLineWithInvalidName() {
            final LineCreateRequest request = new LineCreateRequest("경의중앙선", "초록색");

            assertThatThrownBy(() -> lineService.createLine(request))
                    .isInstanceOf(InvalidLineNameException.class);
        }
    }
}
