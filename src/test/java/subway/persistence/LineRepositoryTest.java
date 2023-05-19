package subway.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static subway.TestFixture.JAMSILNARU_ID;
import static subway.TestFixture.JAMSILSAENAE_ID;
import static subway.TestFixture.JAMSIL_ID;
import static subway.TestFixture.몽촌토성역;
import static subway.TestFixture.몽촌토성역_ID;
import static subway.TestFixture.몽촌토성역_잠실역;
import static subway.TestFixture.석촌역;
import static subway.TestFixture.석촌역_ID;
import static subway.TestFixture.잠실나루역;
import static subway.TestFixture.잠실나루역_잠실역;
import static subway.TestFixture.잠실새내역;
import static subway.TestFixture.잠실역;
import static subway.TestFixture.잠실역_석촌역;
import static subway.TestFixture.잠실역_잠실새내역;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Line;
import subway.persistence.dto.LineDto;
import subway.persistence.dto.SectionDto;
import subway.persistence.exception.NoSuchLineException;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    private static final long LINE_ID = 1L;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final long OTHER_LINE_ID = 2L;
    private static final String OTHER_LINE_NAME = "8호선";
    private static final String OTHER_LINE_COLOR = "pink";
    @InjectMocks
    private LineRepository lineRepository;
    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;

    private Line line;

    @BeforeEach
    void setUp() {
        Line line = new Line(LINE_NAME, LINE_COLOR);
        line.add(잠실나루역_잠실역);
        line.add(잠실역_잠실새내역);
        this.line = line;
    }

    @DisplayName("호선을 저장한다")
    @Test
    void save() {
        doReturn(new LineDto(LINE_ID, LINE_NAME, LINE_COLOR)).when(lineDao).insert(any());
        doNothing().when(sectionDao).insertAll(anyList());

        Line saved = lineRepository.save(line);

        verify(sectionDao, times(1)).insertAll(anyList());
        assertThat(saved.getId()).isEqualTo(LINE_ID);
    }

    @DisplayName("호선을 수정한다")
    @Test
    void update() {
        doNothing().when(sectionDao).insertAll(anyList());
        Line savedLine = new Line(LINE_ID, LINE_NAME, LINE_COLOR, line.getSections());

        lineRepository.save(savedLine);

        verify(lineDao, never()).insert(any());
        verify(sectionDao, times(1)).insertAll(anyList());
    }

    @DisplayName("id로 호선을 조회한다")
    @Test
    void findById() {
        doReturn(new LineDto(LINE_ID, LINE_NAME, LINE_COLOR)).when(lineDao).findById(any());
        doReturn(SectionDto.of(LINE_ID, List.of(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        ))).when(sectionDao).findAllByLineId(anyLong());
        doReturn(잠실역).when(stationDao).findById(eq(JAMSIL_ID));
        doReturn(잠실나루역).when(stationDao).findById(eq(JAMSILNARU_ID));
        doReturn(잠실새내역).when(stationDao).findById(eq(JAMSILSAENAE_ID));

        Line found = lineRepository.findBy(LINE_ID);

        verify(sectionDao, times(1)).findAllByLineId(eq(LINE_ID));
        verify(stationDao, times(4)).findById(anyLong());
        assertThat(found.getSections()).containsExactly(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        );
    }

    @DisplayName("모든 호선을 조회한다")
    @Test
    void findAll() {
        doReturn(List.of(
                new LineDto(LINE_ID, LINE_NAME, LINE_COLOR),
                new LineDto(OTHER_LINE_ID, OTHER_LINE_NAME, OTHER_LINE_COLOR)
        )).when(lineDao).findAll();
        doReturn(SectionDto.of(LINE_ID, List.of(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        ))).when(sectionDao).findAllByLineId(eq(LINE_ID));
        doReturn(SectionDto.of(LINE_ID, List.of(
                몽촌토성역_잠실역,
                잠실역_석촌역
        ))).when(sectionDao).findAllByLineId(eq(OTHER_LINE_ID));
        doReturn(잠실역).when(stationDao).findById(eq(JAMSIL_ID));
        doReturn(잠실나루역).when(stationDao).findById(eq(JAMSILNARU_ID));
        doReturn(잠실새내역).when(stationDao).findById(eq(JAMSILSAENAE_ID));
        doReturn(몽촌토성역).when(stationDao).findById(eq(몽촌토성역_ID));
        doReturn(석촌역).when(stationDao).findById(eq(석촌역_ID));

        List<Line> lines = lineRepository.findAll();

        verify(sectionDao, times(1)).findAllByLineId(eq(LINE_ID));
        verify(sectionDao, times(1)).findAllByLineId(eq(OTHER_LINE_ID));
        verify(stationDao, times(8)).findById(anyLong());
        assertThat(lines.get(0).getSections()).containsExactly(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        );
        assertThat(lines.get(1).getSections()).containsExactly(
                몽촌토성역_잠실역,
                잠실역_석촌역
        );
    }

    @DisplayName("호선을 제거한다")
    @Test
    void deleteById() {
        doReturn(1).when(lineDao).deleteById(any());
        doReturn(1).when(sectionDao).deleteAllByLineId(any());
        Line savedLine = new Line(LINE_ID, LINE_NAME, LINE_COLOR, line.getSections());

        lineRepository.delete(savedLine);

        verify(lineDao, times(1)).deleteById(eq(LINE_ID));
        verify(sectionDao, times(1)).deleteAllByLineId(eq(LINE_ID));
    }

    @DisplayName("호선이 제거되지 않으면 예외를 던진다")
    @Test
    void deleteById_fail_throws() {
        doReturn(0).when(lineDao).deleteById(any());
        doReturn(1).when(sectionDao).deleteAllByLineId(any());
        Line savedLine = new Line(LINE_ID, LINE_NAME, LINE_COLOR, line.getSections());

        assertThatThrownBy(() -> lineRepository.delete(savedLine))
                .isInstanceOf(NoSuchLineException.class);

        verify(lineDao, times(1)).deleteById(eq(LINE_ID));
        verify(sectionDao, times(1)).deleteAllByLineId(eq(LINE_ID));
    }
}
