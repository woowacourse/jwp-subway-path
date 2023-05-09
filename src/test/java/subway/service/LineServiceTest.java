package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private LineService lineService;

    @Test
    public void 호선을_생성한다() {
        //given
        final LineRequest lineRequest = new LineRequest("name", "color");
        final Line savedLine = new Line(1L, "name", "color");
        given(lineRepository.save(any()))
            .willReturn(savedLine);

        //when
        final LineResponse lineResponse = lineService.saveLine(lineRequest);

        //then
        assertThat(lineResponse.getName()).isEqualTo(savedLine.getName());
        assertThat(lineResponse).usingRecursiveComparison()
            .isEqualTo(new LineResponse(1L, "name", "color"));
    }
}
