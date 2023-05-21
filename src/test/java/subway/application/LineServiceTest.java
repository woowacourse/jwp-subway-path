package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineDto;
import subway.dto.LineSaveDto;

@ExtendWith(SpringExtension.class)
class LineServiceTest {

    @Mock
    LineDao lineDao;

    @Mock
    StationDao stationDao;

    @Mock
    SectionDao sectionDao;

    @InjectMocks
    LineService lineService;

    @DisplayName("해당 호선 저장 테스트")
    @Test
    void saveLineSuccessTest() {
        LineSaveDto saveDto = new LineSaveDto("2호선");
        when(lineDao.insert(any(LineEntity.class)))
                .thenReturn(1L);

        LineDto lineDto = lineService.saveLine(saveDto);

        assertThat(lineDto.getId()).isEqualTo(1L);

        verify(lineDao, atLeastOnce()).insert(any(LineEntity.class));
    }

    @DisplayName("전체 노선 조회 테스트")
    @Test
    void findLinesSuccessTest() {
        LineEntity lineEntity1 = new LineEntity(1L, "1호선");
        LineEntity lineEntity2 = new LineEntity(2L, "2호선");
        when(lineDao.findAll())
                .thenReturn(List.of(lineEntity1, lineEntity2));

        lineService.findLineResponses();

        verify(lineDao, atLeastOnce()).findAll();
    }

}