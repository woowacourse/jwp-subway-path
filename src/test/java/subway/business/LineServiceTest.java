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
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.StationEntity;
import subway.presentation.dto.response.LineDetailResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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

        final LineEntity lineEntity = LineEntity.of(lineId, new LineEntity(lineDto.getName(), lineDto.getColor()));
        final StationEntity firstStation = new StationEntity(firstStationId, sectionCreateDto.getFirstStation());
        final StationEntity lastStation = new StationEntity(lastStationId, sectionCreateDto.getLastStation());

        given(lineDao.insert(any())).willReturn(lineEntity);
        given(stationDao.findByName(sectionCreateDto.getFirstStation())).willReturn(firstStation);
        given(stationDao.findByName(sectionCreateDto.getLastStation())).willReturn(lastStation);
        given(sectionDao.insert(any())).willReturn(any());

        // when, then
        assertThat(lineService.save(lineDto, sectionCreateDto)).isEqualTo(1L);
    }

    @Test
    @DisplayName("생성 실패 - 중복된 노선 이름")
    void save_fail_duplicated_line_name() {
        // given
        final LineDto lineDto = new LineDto("신분당선", "bg-gogi-600");
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(10, "강남", "신사");

        given(lineDao.insert(any())).willThrow(DuplicatedLineNameException.class);

        // when, then
        assertThatThrownBy(() -> lineService.save(lineDto, sectionCreateDto))
                .isInstanceOf(DuplicatedLineNameException.class);
    }

    @Test
    @DisplayName("전체 조회 성공")
    void findAll_success() {
        // given
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 2, 1L, "1호선", "bg-blue-600", 1L, "청량리", 2L, "회기"),
                new SectionDetailEntity(2L, 3, 1L, "1호선", "bg-blue-600", 2L, "회기", 3L, "외대앞"),
                new SectionDetailEntity(3L, 4, 2L, "2호선", "bg-green-600", 4L, "시청", 5L, "을지로입구"),
                new SectionDetailEntity(4L, 3, 2L, "2호선", "bg-green-600", 5L, "을지로입구", 6L, "을지로3가")
        );
        given(sectionDao.findSectionDetail()).willReturn(sectionDetailEntities);

        // when
        final List<LineDetailResponse> responses = lineService.findAll();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.get(0).getId()).isEqualTo(1L),
                () -> assertThat(responses.get(0).getName()).isEqualTo("1호선"),
                () -> assertThat(responses.get(0).getColor()).isEqualTo("bg-blue-600"),
                () -> assertThat(responses.get(0).getStations()).hasSize(3),
                () -> assertThat(responses.get(0).getStations().get(1).getId()).isEqualTo(2L),
                () -> assertThat(responses.get(0).getStations().get(1).getName()).isEqualTo("회기")
        );
    }

}
