package subway.service;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.global.exception.line.CanNotDuplicatedLineNameException;
import subway.service.dto.RegisterLineRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineCommandServiceTest extends IntegrationTestHelper {

    @Autowired
    private LineCommandService lineCommandService;

    @Autowired
    private LineDao lineDao;

    @Test
    @DisplayName("deleteLine() : line id 가 주어졌을 때 해당 라인을 삭제할 수 있다.")
    void test_deleteLine() throws Exception {
        //given
        final long lineId = 1L;

        //when
        lineCommandService.deleteLine(lineId);

        //then
        final Optional<LineEntity> savedLine = lineDao.findByLineId(lineId);

        assertThat(savedLine).isEmpty();
    }

    @Test
    @DisplayName("registerLine() : 새로운 line을 생성할 수 있다.")
    void test_registerLine() throws Exception {
        //given
        final String currentStationName = "D";
        final String nextStationName = "Z";
        final String lineName = "line";
        final int distance = 4;

        final RegisterLineRequest registerLineRequest =
                new RegisterLineRequest(currentStationName, nextStationName, lineName, distance);

        final int beforeSize = lineDao.findAll().size();

        //when
        lineCommandService.registerLine(registerLineRequest);

        //then
        final int afterSize = lineDao.findAll().size();
        assertEquals(afterSize, beforeSize + 1);
    }

    @Test
    @DisplayName("registerLine() : line을 새로 생성할 때, 기존에 있는 lineName이 존재한다면 CanNotDuplicatedLineNameException이 발생한다.")
    void test_registerLine_CanNotDuplicatedLineNameException() throws Exception {
        //given
        final String currentStationName = "D";
        final String nextStationName = "Z";
        final String lineName = "1호선";
        final int distance = 4;

        final RegisterLineRequest registerLineRequest =
                new RegisterLineRequest(currentStationName, nextStationName, lineName, distance);

        //when & then
        assertThatThrownBy(() -> lineCommandService.registerLine(registerLineRequest))
                .isInstanceOf(CanNotDuplicatedLineNameException.class);
    }
}
