package subway.application.price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.path.Path;
import subway.domain.price.Price;
import subway.domain.station.Station;

@ExtendWith(MockitoExtension.class)
class LineExtraFeePricePolicyTest {

    @InjectMocks
    LineExtraFeePricePolicy pricePolicy;

    @Mock
    LineDao lineDao;

    @Test
    @DisplayName("노선의 추가 요금이 계산되어야 한다.")
    void calculate_success() {
        // given
        Path path = new Path(List.of(
                new Station("서울역"),
                new Station("용산역"),
                new Station("잠실역")
        ), 20, Set.of(1L));

        given(lineDao.findById(anyLong()))
                .willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-green-300", 500)));

        // when
        Price price = pricePolicy.calculate(path);

        // then
        assertThat(price.getAmount())
                .isEqualTo(500);
    }

    @Test
    @DisplayName("노선이 여러 개 있으면 가장 높은 요금이 계산되어야 한다.")
    void calculate_selectMaxPrice() {
        // given
        Path path = new Path(List.of(
                new Station("서울역"),
                new Station("용산역"),
                new Station("잠실역"),
                new Station("상도역")
        ), 20, Set.of(1L, 2L, 3L));
        given(lineDao.findById(1L))
                .willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-green-300", 500)));
        given(lineDao.findById(2L))
                .willReturn(Optional.of(new LineEntity(2L, "2호선", "bg-green-300", 1000)));
        given(lineDao.findById(3L))
                .willReturn(Optional.of(new LineEntity(3L, "3호선", "bg-green-300", 2000)));

        // when
        Price price = pricePolicy.calculate(path);

        // then
        assertThat(price.getAmount())
                .isEqualTo(2000);
    }
}
