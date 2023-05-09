package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import subway.exception.BusinessException;

class InterStationTest {

    private final Station start = new Station("test1");
    private final Station end = new Station("test2");

    @Test
    public void 역사이의_길이는_양수이어야한다() {
        //given
        final long value = -1L;

        //when
        //then
        assertThatThrownBy(() -> new InterStation(start, end, value))
            .isInstanceOf(BusinessException.class);
    }
    
}
