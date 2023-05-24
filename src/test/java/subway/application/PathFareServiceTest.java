package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.PassengerDto;
import subway.dto.response.PathAndFareResponse;
import subway.integration.IntegrationTest;

@Sql("/setUpStationPath.sql")
class PathFareServiceTest extends IntegrationTest {

    //given
    //   (1)   (2)   (3)
    // 1  -  2  -  3 - 7
    //       |         | (10)
    //       4-5-6 -8 -9

    @Autowired
    private PathFareService pathFareService;

    @Test
    public void distance_line_policy_success() {
        //when
        PathAndFareResponse response = pathFareService.calculateRouteFare(1L, 2L, new PassengerDto(30));

        //then
        //기본1250 + 1호선 100원 추가 = 1350
        Assertions.assertThat(response.getMoney()).isEqualTo(1350);
    }

    @Test
    public void distance_age_line_policy_success() {
        //when
        PathAndFareResponse response = pathFareService.calculateRouteFare(1L, 9L, new PassengerDto(10));

        //then
        //거리비례 1450 - 공제금액 350 / 1100 -> 어린이50%할인 /550 ->  1호선 100원 추가 = 650
        Assertions.assertThat(response.getMoney()).isEqualTo(650);
    }
}