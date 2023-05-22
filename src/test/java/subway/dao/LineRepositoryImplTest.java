package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.LINE_NOT_FOUND;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.이호선_구간;
import static subway.fixture.LineFixture.이호선_엔티티;
import static subway.fixture.LineFixture.이호선_팔호선_구간;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.line.LineExtraFare;
import subway.domain.line.LineName;
import subway.domain.line.LineWithSectionRes;
import subway.exception.DBException;
import subway.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class LineRepositoryImplTest {

    @Mock
    private LineDao lineDao;

    @InjectMocks
    private LineRepositoryImpl lineRepository;

    @Test
    @DisplayName("노선 정보를 저장한다")
    void insert() {
        // given
        when(lineDao.insert(any()))
            .thenReturn(1L);

        // when
        final Long 저장된_이호선_아이디 = lineRepository.insert(이호선);

        // then
        assertThat(저장된_이호선_아이디)
            .isSameAs(1L);
    }

    @Test
    @DisplayName("특정 노선에 존재하는 구간 정보를 조회한다.")
    void findWithSectionsByLineId() {
        // given
        when(lineDao.findByLindIdWithSections(anyLong()))
            .thenReturn(이호선_구간());

        // when
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(1L);

        // then
        assertThat(lineWithSections)
            .extracting(LineWithSectionRes::getLineId, LineWithSectionRes::getLineName,
                LineWithSectionRes::getLineColor, LineWithSectionRes::getExtraFare,
                LineWithSectionRes::getSourceStationId, LineWithSectionRes::getSourceStationName,
                LineWithSectionRes::getTargetStationId, LineWithSectionRes::getTargetStationName,
                LineWithSectionRes::getDistance)
            .contains(
                tuple(1L, "이호선", "bg-green-600", 0, 1L, "잠실역", 2L, "선릉역", 10),
                tuple(1L, "이호선", "bg-green-600", 0, 2L, "선릉역", 3L, "강남역", 10)
            );
    }

    @Test
    @DisplayName("모든 노선에 존재하는 구간 정보를 조회한다.")
    void findAllWithSections() {
        // given
        when(lineDao.findAllWithSections())
            .thenReturn(이호선_팔호선_구간());

        // when
        final List<LineWithSectionRes> lineWithSections = lineRepository.findAllWithSections();

        // then
        assertThat(lineWithSections)
            .extracting(LineWithSectionRes::getLineId, LineWithSectionRes::getLineName,
                LineWithSectionRes::getLineColor, LineWithSectionRes::getExtraFare,
                LineWithSectionRes::getSourceStationId, LineWithSectionRes::getSourceStationName,
                LineWithSectionRes::getTargetStationId, LineWithSectionRes::getTargetStationName,
                LineWithSectionRes::getDistance)
            .contains(
                tuple(1L, "이호선", "bg-green-600", 0, 1L, "잠실역", 2L, "선릉역", 10),
                tuple(1L, "이호선", "bg-green-600", 0, 2L, "선릉역", 3L, "강남역", 10),
                tuple(2L, "팔호선", "bg-pink-600", 0, 5L, "복정역", 6L, "남위례역", 10),
                tuple(2L, "팔호선", "bg-pink-600", 0, 6L, "남위례역", 7L, "산성역", 10)
            );
    }

    @Test
    @DisplayName("유효한 노선 아이디가 주어지면, 노선 정보를 조회한다")
    void findById_success() {
        // given
        when(lineDao.findById(anyLong()))
            .thenReturn(Optional.of(이호선_엔티티));

        // when
        final Line line = lineRepository.findById(1L);

        // then
        assertThat(line)
            .extracting(Line::name, Line::color, Line::extraFare)
            .containsExactly(new LineName("이호선"), "bg-green-600", new LineExtraFare(0));
    }

    @Test
    @DisplayName("유효하지 않은 노선 아이디가 주어지면, 예외가 발생한다.")
    void findById_fail() {
        // given
        when(lineDao.findById(anyLong()))
            .thenReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> lineRepository.findById(1L))
            .isInstanceOf(NotFoundException.class)
            .extracting("errorCode", "errorMessage")
            .containsExactly(LINE_NOT_FOUND, "노선 정보가 존재하지 않습니다. id = 1");
    }

    @Test
    @DisplayName("주어진 노선 아이디에 해당하는 노선 정보를 수정한다.")
    void updateById_success() {
        // given
        when(lineDao.update(any()))
            .thenReturn(1);

        // expected
        assertDoesNotThrow(() -> lineRepository.updateById(1L, 이호선));
    }

    @ParameterizedTest(name = "노선 수정이 발생한 행이 1개가 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void updateById_fail(final int updatedCount) {
        // given
        when(lineDao.update(any()))
            .thenReturn(updatedCount);

        // expected
        assertThatThrownBy(() -> lineRepository.updateById(1L, 이호선))
            .isInstanceOf(DBException.class)
            .hasMessage("DB 업데이트가 정상적으로 진행되지 않았습니다.");
    }

    @Test
    @DisplayName("주어진 노선 아이디에 해당하는 노선 정보를 제거한다.")
    void deleteById_success() {
        // given
        when(lineDao.deleteById(anyLong()))
            .thenReturn(1);

        // expected
        assertDoesNotThrow(() -> lineRepository.deleteById(1L));
    }

    @ParameterizedTest(name = "노선 제거가 발생한 행이 1개가 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void deleteById_fail(final int deletedCount) {
        // given
        when(lineDao.deleteById(anyLong()))
            .thenReturn(deletedCount);

        // expected
        assertThatThrownBy(() -> lineRepository.deleteById(1L))
            .isInstanceOf(DBException.class)
            .hasMessage("DB 삭제가 정상적으로 진행되지 않았습니다.");
    }

    @ParameterizedTest(name = "주어진 이름을 가진 노선이 존재하면 true를, 아니면 false를 반환한다.")
    @CsvSource(value = {"이호선:true", "팔호선:false"}, delimiter = ':')
    void existByName(final String name, final boolean expected) {
        // given
        when(lineDao.existByName(name))
            .thenReturn(expected);

        // expected
        assertThat(lineRepository.existByName(name))
            .isSameAs(expected);
    }

    @Test
    @DisplayName("출발역에서 도착역으로 갈 수 있는 노선들이 가진 모든 구간 정보를 구한다.")
    void getPossibleSections() {
        // given
        when(lineDao.getAllLineSectionsSourceAndTargetStationId(anyLong(), anyLong()))
            .thenReturn(이호선_팔호선_구간());

        // when
        final List<LineWithSectionRes> lineWithSections = lineRepository.getPossibleSections(1L, 7L);

        // then
        assertThat(lineWithSections)
            .extracting(LineWithSectionRes::getLineId, LineWithSectionRes::getLineName,
                LineWithSectionRes::getLineColor, LineWithSectionRes::getExtraFare,
                LineWithSectionRes::getSourceStationId, LineWithSectionRes::getSourceStationName,
                LineWithSectionRes::getTargetStationId, LineWithSectionRes::getTargetStationName,
                LineWithSectionRes::getDistance)
            .contains(
                tuple(1L, "이호선", "bg-green-600", 0, 1L, "잠실역", 2L, "선릉역", 10),
                tuple(1L, "이호선", "bg-green-600", 0, 2L, "선릉역", 3L, "강남역", 10),
                tuple(2L, "팔호선", "bg-pink-600", 0, 5L, "복정역", 6L, "남위례역", 10),
                tuple(2L, "팔호선", "bg-pink-600", 0, 6L, "남위례역", 7L, "산성역", 10)
            );
    }
}
