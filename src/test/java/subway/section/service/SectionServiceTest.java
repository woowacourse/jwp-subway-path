package subway.section.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.dao.SectionDao;
import subway.domain.line.dto.SectionCreateRequest;
import subway.domain.line.entity.SectionEntity;
import subway.domain.line.service.SectionService;
import subway.section.dao.StubSectionDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class SectionServiceTest {

    private StubSectionDao stubSectionDao;
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        stubSectionDao = new StubSectionDao();
        sectionService = new SectionService(stubSectionDao);
    }

    @DisplayName("위 아래 모두 구간이 존재하지않으면 예외를 발생시킨다.")
    @Test
    void deleteSectionWhenNoSection() {
        assertThatThrownBy(() -> sectionService.deleteSection(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되어있지 않은 역은 지울 수 없습니다.");
    }

    @DisplayName("아랫쪽 구간만 존재하면 그 구간을 지운다.")
    @Test
    void deleteSectionWhenNoUpSection() {
        final SectionEntity saved = stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        final Long sectionId = saved.getId();
        sectionService.deleteSection(1L, 2L);
        final Optional<SectionEntity> result = stubSectionDao.findById(sectionId);
        Assertions.assertThat(result).isEmpty();
    }

    @DisplayName("윗쪽 구간만 존재하면 그 구간을 지운다.")
    @Test
    void deleteSectionWhenNoDownSection() {
        final SectionEntity saved = stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        final Long sectionId = saved.getId();
        sectionService.deleteSection(1L, 3L);
        final Optional<SectionEntity> result = stubSectionDao.findById(sectionId);
        Assertions.assertThat(result).isEmpty();
    }

    @DisplayName("윗 아래 모두 구간이 존재하면 두 구간을 합친다.")
    @Test
    void deleteSection() {
        stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        stubSectionDao.insert(new SectionEntity(1L, 3L, 4L, 5));

        sectionService.deleteSection(1L, 3L);

        final Optional<SectionEntity> section = stubSectionDao.findNeighborUpSection(1L, 4L);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isPositive(),
                () -> assertThat(section.get().getUpStationId()).isEqualTo(2L),
                () -> assertThat(section.get().getDownStationId()).isEqualTo(4L),
                () -> assertThat(section.get().getDistance()).isEqualTo(9)
        );
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class CreateSectionServiceTest {

        @Mock
        private SectionDao sectionDao;
        @InjectMocks
        private SectionService createSectionService;

        @DisplayName("인접한 역이 없을 때 기준역 위에 역을 추가한 경우 만든 구간 하나가 반환된다.")
        @Test
        void createSectionToUpWhenNoNeighbor() {
            given(sectionDao.findNeighborSection(anyLong(), anyLong(), any())).willReturn(Optional.empty());
            given(sectionDao.insert(any(SectionEntity.class))).willAnswer(invocation -> {
                final SectionEntity inputSectionEntity = invocation.getArgument(0);
                return inputSectionEntity;
            });
            final List<SectionEntity> sectionEntities = createSectionService.createSection(new SectionCreateRequest(1L, 2L, 3L, true, 5));
            assertThat(sectionEntities).containsExactly(new SectionEntity(null, 1L, 3L, 2L, 5));
        }

        @DisplayName("인접한 역이 없을 때 기준역 아래에 역을 추가한 경우 만든 구간 하나가 반환된다.")
        @Test
        void createSectionToDownWhenNoNeighbor() {
            given(sectionDao.findNeighborSection(anyLong(), anyLong(), any())).willReturn(Optional.empty());
            given(sectionDao.insert(any(SectionEntity.class))).willAnswer(invocation -> {
                final SectionEntity inputSectionEntity = invocation.getArgument(0);
                return inputSectionEntity;
            });
            final List<SectionEntity> sectionEntities = createSectionService.createSection(new SectionCreateRequest(1L, 2L, 3L, false, 5));
            assertThat(sectionEntities).containsExactly(new SectionEntity(null, 1L, 2L, 3L, 5));
        }

        @DisplayName("인접한 역이 있을 때 존재하는 구간의 거리가 더 작으면 예외를 발생시킨다.")
        @ParameterizedTest
        @ValueSource(ints = {5, 6, 7})
        void throwExceptionWhenExistDistanceIsLowerThanAddedDistance(final int distance) {
            given(sectionDao.findNeighborSection(anyLong(), anyLong(), any())).willReturn(Optional.of(new SectionEntity(1L, 2L, 3L, 5)));

            assertThatThrownBy(() -> createSectionService.createSection(new SectionCreateRequest(1L, 2L, 3L, true, distance)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }

        @DisplayName("인접한 역이 없을 때 기준역 위에 역을 추가한 경우 만든 구간 두 개가 반환된다.")
        @Test
        void divideSectionByAddedStationWhenUp() {
            given(sectionDao.findNeighborSection(anyLong(), anyLong(), any())).willReturn(Optional.of(new SectionEntity(1L, 2L, 3L, 5)));
            given(sectionDao.insert(any(SectionEntity.class))).willAnswer(invocation -> {
                final SectionEntity inputSectionEntity = invocation.getArgument(0);
                return inputSectionEntity;
            });

            final List<SectionEntity> sectionEntities = createSectionService.createSection(new SectionCreateRequest(1L, 3L, 4L, true, 4));
            assertThat(sectionEntities).containsExactly(
                    new SectionEntity(1L, 2L, 4L, 1),
                    new SectionEntity(1L, 4L, 3L, 4)
            );
        }

        @DisplayName("인접한 역이 없을 때 기준역 아래에 역을 추가한 경우 만든 구간 두 개가 반환된다.")
        @Test
        void divideSectionByAddedStationWhenDown() {
            given(sectionDao.findNeighborSection(anyLong(), anyLong(), any())).willReturn(Optional.of(new SectionEntity(1L, 2L, 3L, 5)));
            given(sectionDao.insert(any(SectionEntity.class))).willAnswer(invocation -> {
                final SectionEntity inputSectionEntity = invocation.getArgument(0);
                return inputSectionEntity;
            });

            final List<SectionEntity> sectionEntities = createSectionService.createSection(new SectionCreateRequest(1L, 2L, 4L, false, 4));
            assertThat(sectionEntities).containsExactly(
                    new SectionEntity(1L, 2L, 4L, 4),
                    new SectionEntity(1L, 4L, 3L, 1)
            );
        }
    }
}
