package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.request.CreateSectionRequest;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.StationFixture.삼성역;
import static subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    @Test
    void 주어진_노선과_상행역_하행역을_이용하여_구간을_생성한다() {
        given(stationRepository.findById(any()))
                .willReturn(삼성역.STATION, 역삼역.STATION);
        given(lineRepository.findById(anyLong()))
                .willReturn(이호선.LINE);

        CreateSectionRequest request = new CreateSectionRequest(삼성역.STATION.getId(), 역삼역.STATION.getId(), 5);

        LineResponse result = sectionService.createSection(이호선.LINE.getId(), request);

        assertAll(
                () -> Assertions.assertThat(result.getName()).isEqualTo(이호선.LINE.getName()),
                () -> Assertions.assertThat(result.getColor()).isEqualTo(이호선.LINE.getColor()),
                () -> Assertions.assertThat(result.getStations())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(삼성역.RESPONSE, 역삼역.RESPONSE))
        );
    }


}
