package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StubLineDao;
import subway.dao.StubSectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.dto.LineRequest;
import subway.dto.LineSearchResponse;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    private StubLineDao stubLineDao;
    private StubSectionDao stubSectionDao;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        stubLineDao = new StubLineDao();
        stubLineDao.insert(new Line(1L, "1호선", "파란색"));
        stubLineDao.insert(new Line(2L, "2호선", "초록색"));
        stubSectionDao = new StubSectionDao();
        lineService = new LineService(stubLineDao, stubSectionDao);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void saveLine() {
        final Long lineId = lineService.saveLine(new LineRequest("2호선", "초록색")).getId();
        final Line result = stubLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록색")
        );
    }

    @DisplayName("노선을 업데이트한다.")
    @Test
    void updateLine() {
        final Long lineId = stubLineDao.insert(new Line("2호선", "초록색")).getId();
        lineService.updateLine(lineId, new LineRequest("1호선", "파란색"));
        final Line result = stubLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("파란색")
        );
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLineById() {
        final Long lineId = stubLineDao.insert(new Line("1호선", "파란색")).getId();
        lineService.deleteLineById(lineId);
        final Line result = stubLineDao.findById(lineId);
        assertThat(result).isNull();
    }

    @DisplayName("lineId로 해당 노선의 모든 구간을 순서대로 가져온다.")
    @Test
    void getLineResponseWithSections() {
        final LineSearchResponse lineSearchResponse = lineService.getLineSearchResponse(1L);
        assertAll(
                () -> assertThat(lineSearchResponse.getId()).isEqualTo(1L),
                () -> assertThat(lineSearchResponse.getName()).isEqualTo("1호선"),
                () -> assertThat(lineSearchResponse.getColor()).isEqualTo("파란색"),
                () -> assertThat(lineSearchResponse.getSections()).containsExactly(
                        Section.builder().id(1L).build(),
                        Section.builder().id(2L).build(),
                        Section.builder().id(3L).build()
                )
        );
    }

    @DisplayName("모든 노선의 모든 구간을 순서대로 가져온다.")
    @Test
    void getLineResponsesWithSections() {
        final List<LineSearchResponse> lineResponsesWithSections = lineService.getLineSearchResponses();
        final LineSearchResponse line1 = lineResponsesWithSections.get(0);
        final LineSearchResponse line2 = lineResponsesWithSections.get(1);
        assertAll(
                () -> assertThat(line1.getId()).isEqualTo(1L),
                () -> assertThat(line1.getName()).isEqualTo("1호선"),
                () -> assertThat(line1.getColor()).isEqualTo("파란색"),
                () -> assertThat(line1.getSections()).containsExactly(
                        Section.builder().id(1L).build(),
                        Section.builder().id(2L).build(),
                        Section.builder().id(3L).build()
                ),
                () -> assertThat(line2.getId()).isEqualTo(2L),
                () -> assertThat(line2.getName()).isEqualTo("2호선"),
                () -> assertThat(line2.getColor()).isEqualTo("초록색"),
                () -> assertThat(line2.getSections()).containsExactly(
                        Section.builder().id(4L).build(),
                        Section.builder().id(5L).build()
                )
        );
    }
}
