package subway.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.dto.LineDto;
import subway.business.dto.SectionCreateDto;
import subway.exception.DuplicatedLineNameException;
import subway.persistence.LineDao;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.StationEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private LineService lineService;

    @Test
    @DisplayName("생성 성공")
    void save_success() {
        // given
        final long lineId = 1L;
        final long firstStationId = 1L;
        final long lastStationId = 2L;
        final LineDto lineDto = new LineDto("신분당선", "bg-red-600");
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(10, "강남", "신사");

        // when
        final LineEntity lineEntity = LineEntity.of(lineId, new LineEntity(lineDto.getName(), lineDto.getColor()));
        final StationEntity firstStation = new StationEntity(firstStationId, sectionCreateDto.getFirstStation());
        final StationEntity lastStation = new StationEntity(lastStationId, sectionCreateDto.getLastStation());

        given(lineDao.insert(any())).willReturn(lineEntity);
        given(stationDao.findByName(sectionCreateDto.getFirstStation())).willReturn(firstStation);
        given(stationDao.findByName(sectionCreateDto.getLastStation())).willReturn(lastStation);
        given(sectionDao.insert(any())).willReturn(any());

        // then
        assertThat(lineService.save(lineDto, sectionCreateDto)).isEqualTo(1L);
    }

    @Test
    @DisplayName("생성 실패 - 중복된 노선 이름")
    void save_fail_duplicated_line_name() {
        // given
        final LineDto lineDto = new LineDto("신분당선", "bg-gogi-600");
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(10, "강남", "신사");

        // when
        given(lineDao.insert(any())).willThrow(DuplicatedLineNameException.class);

        // then
        assertThatThrownBy(() -> lineService.save(lineDto, sectionCreateDto))
                .isInstanceOf(DuplicatedLineNameException.class);
    }

}
