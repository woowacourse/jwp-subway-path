package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static subway.fixture.LineFixture.이호선;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Test
    void 주어진_노선을_저장한다() {
        given(lineRepository.insert(any()))
                .willReturn(이호선.LINE);

        LineResponse lineResponse = lineService.saveLine(이호선.REQUEST);

        Assertions.assertThat(lineResponse).isNotNull();
    }


    @Test
    void 주어진_id에_해당하는_노선을_조회한다() {
        given(lineRepository.findById(anyLong()))
                .willReturn(이호선.LINE);

        LineResponse lineResponse = lineService.findLineById(1L);

        Assertions.assertThat(lineResponse).isNotNull();
    }

    @Test
    void 모든_노선을_조회한다() {
        given(lineRepository.findAll())
                .willReturn(List.of(이호선.LINE));

        List<LineResponse> lineResponse = lineService.findAllLine();

        Assertions.assertThat(lineResponse).isNotNull();
    }
}
