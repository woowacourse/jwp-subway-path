package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.*;
import subway.domain.Line;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.exception.LineAlreadyExistException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @InjectMocks
    private LineService lineService;

    @Mock
    private SubwayGraphs subwayGraphs;


    @Test
    @DisplayName("새로운 노선을 생성한다.")
    void create_line() {
        final LineCreateRequest request = new LineCreateRequest("1호선");
        given(lineDao.findByName("1호선")).willReturn(Optional.empty());
        LineEntity lineEntity = new LineEntity(1L, "1호선");
        given(lineDao.saveLine(any())).willReturn(lineEntity);

        LineResponse lineResponse = lineService.createLine(request);

        Assertions.assertThat(lineResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("기존에 노선이 존재하면 예외가 발생한다.")
    void create_line1() {
        String lineName = "1호선";
        final LineCreateRequest request = new LineCreateRequest(lineName);
        LineEntity lineEntity = new LineEntity(1L, lineName);
        given(lineDao.findByName(lineName)).willReturn(
                Optional.ofNullable(lineEntity));

        Assertions.assertThatThrownBy(() -> lineService.createLine(request))
                .isInstanceOf(LineAlreadyExistException.class);
    }


}

