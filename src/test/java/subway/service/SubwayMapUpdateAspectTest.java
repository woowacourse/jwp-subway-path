package subway.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.dao.SectionDao;
import subway.entity.SectionEntity;

@SpringBootTest
class SubwayMapUpdateAspectTest {

    @Autowired
    private SectionCreateService sectionCreateService;
    @Autowired
    private SectionDeleteService sectionDeleteService;
    @MockBean
    private SubwayMapService subwayMapService;
    @MockBean
    private SectionDao sectionDao;

    @DisplayName("구간 생성 시 지하철 노선도가 업데이트된다")
    @Test
    void shouldUpdateWhenCreateSection() {
        // given
        given(sectionDao.findByLineId(anyLong())).willReturn(Collections.emptyList());
        given(sectionDao.insert(any())).willReturn(new SectionEntity(1L, 2L, 3L, 4L, 5));

        // when
        sectionCreateService.createSection(1L, 1L, 2L, true, 10);

        // then
        verify(subwayMapService, times(1)).update();
    }

    @DisplayName("구간 삭제 시 지하철 노선도가 업데이트된다")
    @Test
    void shouldUpdateWhenDeleteSection() {
        // given
        given(sectionDao.findNeighborUpSection(anyLong(), anyLong())).willReturn(Optional.empty());
        given(sectionDao.findNeighborDownSection(anyLong(), anyLong())).willReturn(
                Optional.of(new SectionEntity(1L, 2L, 3L, 4L, 5))
        );
        doNothing().when(sectionDao).deleteById(anyLong());

        // when
        sectionDeleteService.deleteSection(1L, 2L);

        // then
        verify(subwayMapService, times(1)).update();
    }
}
