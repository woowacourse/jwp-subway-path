package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.dao.StubSectionDao;
import subway.domain.section.Section;
import subway.dto.SectionCreateRequest;

class SectionCreateServiceTest {

    private StubSectionDao stubSectionDao;
    private SectionCreateService sectionCreateService;

    @BeforeEach
    void setUp() {
        stubSectionDao = new StubSectionDao();
        sectionCreateService = new SectionCreateService(stubSectionDao);
    }

    @DisplayName("노선이 비어있을 때 새로운 역을 위로 추가하면 (addedId, baseId) 인 구간이 추가된다.")
    @Test
    void createSectionToUpInEmptyLine() {
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 3L, true, 5);
        final List<Section> sections = sectionCreateService.createSection(sectionCreateRequest);
        assertThat(sections).containsExactly(
                Section.builder()
                        .id(1L)
                        .lineId(1L)
                        .upStation(3L)
                        .downStation(2L)
                        .distance(5)
                        .build()
        );
    }

    @DisplayName("노선이 비어있을 때 새로운 역을 아래로 추가하면 (baseId, addedId) 인 구간이 추가된다.")
    @Test
    void createSectionToDownInEmptyLine() {
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 3L, false, 5);
        final List<Section> sections = sectionCreateService.createSection(sectionCreateRequest);
        assertThat(sections).containsExactly(
                Section.builder()
                        .id(1L)
                        .lineId(1L)
                        .upStation(2L)
                        .downStation(3L)
                        .distance(5)
                        .build()
        );
    }

    @DisplayName("구간이 저장되어 있을 때")
    @Nested
    class DescribeSectionSaved {

        @BeforeEach
        void setUp() {
            stubSectionDao.insert(
                    Section.builder()
                            .lineId(1L)
                            .upStation(1L)
                            .downStation(2L)
                            .distance(4)
                            .build()
            );
        }

        @DisplayName("노선이 비어있지 않을 때 baseId를 포함한 구간이 없으면 예외를 발생시킨다.")
        @Test
        void throwExceptionWhenBaseIdNoExistInNotEmptyLine() {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 3L, 2L, false, 5);
            assertThatThrownBy(() -> sectionCreateService.createSection(sectionCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("기준점이 되는 역은 이미 구간에 존재해야 합니다.");
        }

        @DisplayName("인접한 역이 없을 때 기준역 아래에 역을 추가한 경우 만든 구간 하나가 반환된다.")
        @Test
        void createSectionToUpWhenNoNeighbor() {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 3L, false, 5);
            final List<Section> result = sectionCreateService.createSection(sectionCreateRequest);
            final List<Section> sections = stubSectionDao.findByLineId(1L);

            assertAll(
                    () -> assertThat(result).containsExactly(
                            Section.builder()
                                    .id(2L)
                                    .lineId(1L)
                                    .upStation(2L)
                                    .downStation(3L)
                                    .distance(5)
                                    .build()
                    ),
                    () -> assertThat(sections).contains(
                            Section.builder()
                                    .id(1L)
                                    .lineId(1L)
                                    .upStation(1L)
                                    .downStation(2L)
                                    .distance(4)
                                    .build(),
                            Section.builder()
                                    .id(2L)
                                    .lineId(1L)
                                    .upStation(2L)
                                    .downStation(3L)
                                    .distance(5)
                                    .build()
                    )
            );
        }

        @DisplayName("인접한 역이 없을 때 기준역 위에 역을 추가한 경우 만든 구간 하나가 반환된다.")
        @Test
        void createSectionToDownWhenNoNeighbor() {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 1L, 3L, true, 5);
            final List<Section> result = sectionCreateService.createSection(sectionCreateRequest);
            final List<Section> sections = stubSectionDao.findByLineId(1L);
            assertAll(
                    () -> assertThat(result).containsExactly(
                            Section.builder()
                                    .id(2L)
                                    .lineId(1L)
                                    .upStation(3L)
                                    .downStation(1L)
                                    .distance(5)
                                    .build()
                    ),
                    () -> assertThat(sections).contains(
                            Section.builder()
                                    .id(2L)
                                    .lineId(1L)
                                    .upStation(3L)
                                    .downStation(1L)
                                    .distance(5)
                                    .build(),
                            Section.builder()
                                    .id(1L)
                                    .lineId(1L)
                                    .upStation(1L)
                                    .downStation(2L)
                                    .distance(4)
                                    .build()
                    )
            );
        }

        @DisplayName("인접한 역이 있을 때 존재하는 구간의 거리가 더 작으면 예외를 발생시킨다.")
        @ParameterizedTest
        @CsvSource({"4", "5", "6"})
        void throwExceptionWhenExistDistanceIsLowerThanAddedDistance(final int distance) {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 3L, true, distance);
            assertThatThrownBy(() -> sectionCreateService.createSection(sectionCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }

        @DisplayName("인접한 역이 없을 때 기준역 위에 역을 추가한 경우 만든 구간 두 개가 반환된다.")
        @Test
        void divideSectionByAddedStationWhenUp() {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 3L, true, 3);
            final List<Section> sections = sectionCreateService.createSection(sectionCreateRequest);
            assertThat(sections).containsExactly(
                    Section.builder()
                            .id(2L)
                            .lineId(1L)
                            .upStation(1L)
                            .downStation(3L)
                            .distance(1)
                            .build(),
                    Section.builder()
                            .id(3L)
                            .lineId(1L)
                            .upStation(3L)
                            .downStation(2L)
                            .distance(3)
                            .build()
            );
        }


        @DisplayName("인접한 역이 없을 때 기준역 아래에 역을 추가한 경우 만든 구간 두 개가 반환된다.")
        @Test
        void divideSectionByAddedStationWhenDown() {
            final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 1L, 3L, false, 3);
            final List<Section> sections = sectionCreateService.createSection(sectionCreateRequest);
            assertThat(sections).containsExactly(
                    Section.builder()
                            .id(2L)
                            .lineId(1L)
                            .upStation(1L)
                            .downStation(3L)
                            .distance(3)
                            .build(),
                    Section.builder()
                            .id(3L)
                            .lineId(1L)
                            .upStation(3L)
                            .downStation(2L)
                            .distance(1)
                            .build()
            );
        }
    }
}
