package subway.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static subway.fixture.Fixture.LINE_SAVE_REQUEST;
import static subway.fixture.Fixture.line2WithOneSection;
import static subway.fixture.Fixture.line2WithTwoSection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.domain.LineRepository;
import subway.business.service.dto.LineResponse;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;
    @Mock
    private LineRepository lineRepository;

    @DisplayName("Line을 생성한다.")
    @Test
    void shouldCreateLineWhenRequest() {
        given(lineRepository.create(any())).willReturn(line2WithOneSection());

        LineResponse lineResponse = lineService.createLine(LINE_SAVE_REQUEST);

        assertThat(lineResponse.getId()).isEqualTo(line2WithOneSection().getId());
        assertThat(lineResponse.getName()).isEqualTo(LINE_SAVE_REQUEST.getName());
        assertThat(lineResponse.getSections().get(0).getUpwardStation())
                .isEqualTo(LINE_SAVE_REQUEST.getUpwardTerminus());
    }

    @DisplayName("Line에 Station을 추가한다.")
    @Test
    void shouldAddStationToLineWhenRequest() {
        given(lineRepository.findById(1L)).willReturn(line2WithTwoSection());

        LineResponse lineResponse = lineService.findLineResponseById(1L);

        assertThat(lineResponse.getId()).isEqualTo(line2WithTwoSection().getId());
        assertThat(lineResponse.getName()).isEqualTo(line2WithOneSection().getName());
        assertThat(lineResponse.getSections()).hasSize(2);
        assertThat(lineResponse.getSections().get(1).getDownwardStation()).isEqualTo("까치산역");
    }
}
