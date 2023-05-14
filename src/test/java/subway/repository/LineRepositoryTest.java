package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.dao.LineDao;
import subway.line.dao.SectionDao;
import subway.line.repository.LineRepository;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static subway.utils.LineEntityFixture.LINE_NUMBER_TWO_ENTITY;
import static subway.utils.LineEntityFixture.NO_ID_LINE_NUMBER_TWO_ENTITY;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionEntityFixture.NO_ID_JAMSIL_TO_JAMSIL_NARU_SECTION_ENTITY;
import static subway.utils.SectionEntityFixture.NO_ID_SULLEUNG_TO_JAMSIL_SECTION_ENTITY;
import static subway.utils.StationEntityFixture.*;

@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    private LineRepository lineRepository;

    @BeforeEach
    void setup() {
        lineRepository = new LineRepository(stationRepository, lineDao, sectionDao);
    }

    @Test
    void createLine() {
        doReturn(Optional.empty()).when(lineDao).findIdByName(LINE_NUMBER_TWO_ENTITY.getName());
        doReturn(LINE_NUMBER_TWO_ENTITY.getId()).when(lineDao).insert(NO_ID_LINE_NUMBER_TWO_ENTITY);
        doReturn(Optional.of(SULLEUNG_STATION_ENTITY.getId())).when(stationRepository)
                                                              .findIdByName(SULLEUNG_STATION_ENTITY.getName());
        doReturn(Optional.of(JAMSIL_STATION_ENTITY.getId())).when(stationRepository)
                                                            .findIdByName(JAMSIL_STATION_ENTITY.getName());
        doReturn(Optional.of(JAMSIL_NARU_STATION_ENTITY.getId())).when(stationRepository)
                                                                 .findIdByName(JAMSIL_NARU_STATION_ENTITY.getName());

        lineRepository.createLine(LINE_NUMBER_TWO);

        verify(sectionDao, times(1)).insertSections(List.of(NO_ID_SULLEUNG_TO_JAMSIL_SECTION_ENTITY, NO_ID_JAMSIL_TO_JAMSIL_NARU_SECTION_ENTITY));
    }

    // 단위 테스트를 최대한 해보려 했지만 별로 의미 없이 과도한 품이 드는 것 같아
    // 영속성에 관한 테스트는 통합 테스트로 대체하도록 하겠습니다!
}
