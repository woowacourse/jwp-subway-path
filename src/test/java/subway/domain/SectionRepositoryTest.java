package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.SectionEntity;
import subway.domain.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionRepositoryTest {

    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("새로운 구간을 저장하고 조회할 수 있다.")
    void save_success() {
        // given
        given(sectionDao.findAllSectionsWithStationNameByLineId(anyLong())).willReturn(List.of(
                new SectionDto(1L, 1L, 2L, "교대역", "강남역", 10),
                new SectionDto(2L, 2L, 3L, "강남역", "역삼역", 20)));
        given(sectionDao.insert(any(SectionEntity.class))).willReturn(1L);

        // when
        sectionRepository.save(1L, new Section(new Station(1L, "교대역"), new Station(2L, "강남역"), Distance.from(10)));
        sectionRepository.save(1L, new Section(new Station(2L, "강남역"), new Station(3L, "역삼역"), Distance.from(20)));

        // then
        assertThat(sectionRepository.findSectionsByLineId(1L))
                .usingRecursiveComparison()
                .isEqualTo(new Sections(List.of(
                        new Section(1L, new Station(1L, "교대역"), new Station(2L, "강남역"), Distance.from(10)),
                        new Section(2L, new Station(2L, "강남역"), new Station(3L, "역삼역"), Distance.from(20)))
                ));
    }

}