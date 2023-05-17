package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/line_data_initialize.sql")
class SectionControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Long lineId = 1L;

    @Test
    @DisplayName("빈 노선에 구간을 추가하면 추가에 성공한다.")
    void addSection_toEmptyLine_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("1개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[0].distance").value(50));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가하면 추가에 성공한다.")
    void addSection_betweenStation_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("잠실역", "대구역", 20);

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("2개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("대구역"))
                .andExpect(jsonPath("$.result[0].distance").value(20))
                .andExpect(jsonPath("$.result[1].startStationName").value("대구역"))
                .andExpect(jsonPath("$.result[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[1].distance").value(30));
    }

    @Test
    @DisplayName("노선의 상행 종점 역 앞에 새로운 역을 추가하면 추가에 성공한다.")
    void addSection_frontOfStartStation_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("대구역", "잠실역", 20);

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("2개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("대구역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].distance").value(20))
                .andExpect(jsonPath("$.result[1].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[1].distance").value(50));
    }

    @Test
    @DisplayName("노선의 하행 종점 역 뒤에 역을 추가하면 추가에 성공한다.")
    void addSection_behindEndStation_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("삼성역", "대구역", 20);

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("2개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[0].distance").value(50))
                .andExpect(jsonPath("$.result[1].startStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[1].endStationName").value("대구역"))
                .andExpect(jsonPath("$.result[1].distance").value(20));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 기준이 되는 역이 없다면 추가에 실패한다.")
    void addSection_betweenStation_noStandard_fail() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("포항역", "대구역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.upBoundStationName").value("노선에 기준이 되는 역을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.validation.downBoundStationName").value("노선에 기준이 되는 역을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 모든 역이 노선에 존재한다면 추가에 실패한다.")
    void addSection_betweenStation_allInLine_fail() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("삼성역", "잠실역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.upBoundStationName").value("노선에 이미 해당 역이 존재합니다."))
                .andExpect(jsonPath("$.validation.downBoundStationName").value("노선에 이미 해당 역이 존재합니다."));
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 51})
    @DisplayName("기존 구간에 역을 추가할 때, 기존 구간보다 길이가 같거나 길다면 추가에 실패한다.")
    void addSection_betweenSection_overExistingSectionDistance(int distance) throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("잠실역", "대구역", distance);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.distance").value("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다."));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("노선에 구간을 추가할 때, 구간의 길이가 0 이하면 추가에 실패한다.")
    void addSection_notPositiveDistance_fail(int distance) throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", distance);

        // expect
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.distance").value("구간의 길이는 1~100 사이여야 합니다."));
    }

    @Test
    @DisplayName("노선에 구간을 추가할 때, 존재하지 않는 역이면 추가에 실패한다. ")
    void addSection_notExistsStation_fail() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("장승배기역", "상도역", 10);

        // expect
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.upBoundStationName").value("추가하려는 역이 존재하지 않습니다."))
                .andExpect(jsonPath("$.validation.downBoundStationName").value("추가하려는 역이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("상행역과 하행역이 같으면 추가에 실패한다.")
    void addSection_sameStationName() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실역", 10);

        // when
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.upBoundStationName").value("추가하려는 상행역과 하행역의 이름이 같습니다."))
                .andExpect(jsonPath("$.validation.downBoundStationName").value("추가하려는 상행역과 하행역의 이름이 같습니다."));
    }

    @Test
    @DisplayName("구간의 중간에 있는 역을 삭제할 때, 두 구간이 합쳐진다.")
    void deleteStation_mergeSection_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("삼성역", "대구역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("삼성역");
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 삭제되었습니다."));

        // then
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$.message").value("1개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("대구역"))
                .andExpect(jsonPath("$.result[0].distance").value(100));
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실역", "삼성역"})
    @DisplayName("노선에 구간이 1개일 때, 모든 구간이 삭제된다.")
    void deleteStation_singleSection_success(String station) throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(station);
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 삭제되었습니다."));

        // then
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$.result.size()").value(0))
                .andExpect(jsonPath("$.message").value("0개의 구간이 조회되었습니다."));
    }

    @Test
    @DisplayName("노선에 구간이 여러 개 일 때, 상행 종점 역을 지울 수 있다.")
    void deleteStation_multipleSection_startStation_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("삼성역", "대구역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("잠실역");
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 삭제되었습니다."));

        // then
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$.message").value("1개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("대구역"))
                .andExpect(jsonPath("$.result[0].distance").value(50));
    }

    @Test
    @DisplayName("노선에 구간이 여러 개 일 때, 하행 종점 역을 지울 수 있다.")
    void deleteStation_multipleSection_endStation_success() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("삼성역", "대구역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("대구역");
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 삭제되었습니다."));

        // then
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$.message").value("1개의 구간이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[0].distance").value(50));
    }

    @Test
    @DisplayName("노선에 없는 역을 지우면 삭제에 실패한다.")
    void deleteStation_noStation_inLine_fail() throws Exception {
        // given
        addSection("잠실역", "삼성역", 50);
        addSection("삼성역", "대구역", 50);

        // expect
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("포항역");
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.stationName").value("해당 되는 역을 찾을 수 없습니다."));
    }

    void addSection(String startStationName, String endStationName, int distance) throws Exception {
        SectionCreateRequest createRequest = new SectionCreateRequest(startStationName, endStationName, distance);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createRequest)));
    }
}
