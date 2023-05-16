package subway.line.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.repository.LineRepository;
import subway.domain.line.service.LineService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private LineService lineService;


    @Test
    void 모든_노선_조회_테스트() {
        // when
        lineService.findAll();

        // then
        verify(lineRepository).findAll();
    }

    @Test
    void 단일_노선_조회_테스트() {
        // when
        lineService.findById(any());

        // then
        verify(lineRepository).findById(any());
    }
}
