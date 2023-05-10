package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.dao.LineDao;

class LineServiceTest {

    @DisplayName("노선을 생성하고 그 노선에 역을 등록한다.")
    @Test
    void registerLine() {
        final LineDao mock = Mockito.mock(LineDao.class);

    }
}
