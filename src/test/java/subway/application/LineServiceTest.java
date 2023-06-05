package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.entity.LineEntity;
import subway.dao.vo.SectionStationMapper;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LineSectionResponse;
import subway.exception.DuplicatedException;
import subway.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;
import static subway.TestFeature.*;

@ExtendWith(MockitoExtension.class)
@Sql("classpath:initializeTestDb.sql")
class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private LineService lineService;

    @DisplayName("LineRequest를 통해 라인 정보를 저장하면 LineResponse를 반환한다")
    @Test
    void saveLine() {
        // given
        String lineName = "3호선";
        String lineColor = "주황색";
        LineEntity lineEntity = new LineEntity(lineName, lineColor);
        LineEntity resultLineEntity = new LineEntity(4L, lineName, lineColor);
        given(lineDao.insert(lineEntity))
                .willReturn(resultLineEntity);

        // when
        LineResponse lineResponse = lineService.saveLine(new LineRequest(lineName, lineColor));

        // then
        assertAll(
                () -> assertThat(lineResponse.getName()).isEqualTo(lineName),
                () -> assertThat(lineResponse.getColor()).isEqualTo(lineColor)
        );
    }

    @DisplayName("이미 존재하는 이름의 노선을 등록하는 경우 예외를 반환한다")
    @Test
    void saveLineExistName() {
        // given
        String lineName = "2호선";
        String lineColor = "주황색";
        given(lineDao.isExistName(lineName)).willReturn(true);

        // then
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest(lineName, lineColor)))
                .isInstanceOf(DuplicatedException.class);
    }

    @DisplayName("이미 존재하는 색의 노선을 등록하는 경우 예외를 반환한다")
    @Test
    void saveLineExistColor() {
        // given
        String lineName = "3호선";
        String lineColor = "초록색";
        given(lineDao.isExistColor(lineColor)).willReturn(true);

        // then
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest(lineName, lineColor)))
                .isInstanceOf(DuplicatedException.class);
    }

    @DisplayName("아이디를 통해 특정 노선의 행을 조회한다")
    @Test
    void findLineResponseById() {
        // given
        Long findId = 1L;
        List<SectionStationMapper> sectionMappers = new ArrayList<>(List.of(SECTION_STATION_MAPPER_봉천_서울대입구, SECTION_STATION_MAPPER_서울대입구_사당));
        List<Section> sections = sectionMappers.stream()
                                               .map(Section::from)
                                               .collect(Collectors.toList());
        given(lineDao.findById(findId)).willReturn(Optional.of(LINE_ENTITY_2호선));
        given(sectionDao.findSectionsByLineId(findId)).willReturn(sectionMappers);

        // when
        LineSectionResponse findLineResponse = lineService.findLineResponseById(findId);

        // then
        assertThat(findLineResponse).isEqualTo(
                LineSectionResponse.of(new Line(findId, LINE_ENTITY_2호선.getName(), LINE_ENTITY_2호선.getColor(), new Sections(sections)))
        );
    }

    @DisplayName("전체 노선 조회 시 각 노선의 역 정보를 반환한다")
    @Test
    void findLineResponses() {
        // given
        List<LineEntity> lineEntities = new ArrayList<>(List.of(LINE_ENTITY_2호선, LINE_ENTITY_1호선));
        given(lineDao.findAll()).willReturn(lineEntities);
        given(sectionDao.findSectionsByLineId(1L)).willReturn(Collections.singletonList(new SectionStationMapper(1L, 1L, "인천역", 2L, "방배역", 5)));
        given(sectionDao.findSectionsByLineId(2L)).willReturn(Collections.singletonList(new SectionStationMapper(2L, 3L, "사당역", 4L, "낙성대역", 5)));

        // when
        List<LineSectionResponse> lineSectionResponses = lineService.findLineResponses();

        // then
        assertThat(lineSectionResponses).containsAll(List.of(
                LineSectionResponse.from(1L, "2호선", "초록색", List.of("인천역", "방배역")),
                LineSectionResponse.from(2L, "1호선", "파랑색", List.of("사당역", "낙성대역"))
        ));
    }

    @DisplayName("특정 노선에 대한 정보를 수정한다")
    @Test
    void updateLine() {
        // given
        Long updateId = 1L;
        LineRequest updateLineRequest = new LineRequest("3호선", "주황색");
        given(lineDao.isExistId(updateId)).willReturn(true);
        doNothing().when(lineDao)
                   .update(any());

        // then
        assertDoesNotThrow(() -> lineService.updateLine(updateId, updateLineRequest));
    }

    @DisplayName("없는 노선에 대한 정보를 수정할 시 예외를 반환한다")
    @Test
    void updateLineException() {
        // given
        Long updateId = 100L;
        given(lineDao.isExistId(updateId)).willReturn(false);
        LineRequest updateLineRequest = new LineRequest("3호선", "주황색");

        // then
        assertThatThrownBy(() -> lineService.updateLine(updateId, updateLineRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("특정 노선에 대한 정보를 삭제한다")
    @Test
    void deleteLineById() {
        // given
        Long updateId = 1L;
        given(lineDao.isExistId(updateId)).willReturn(true);

        // then
        assertDoesNotThrow(() -> lineService.deleteLineById(updateId));
    }

    @DisplayName("없는 노선에 대한 정보를 삭제할 시 예외를 반환한다")
    @Test
    void deleteLineByIdException() {
        // given
        Long updateId = 100L;
        given(lineDao.isExistId(updateId)).willReturn(false);

        // then
        assertThatThrownBy(() -> lineService.deleteLineById(updateId))
                .isInstanceOf(NotFoundException.class);
    }
}
