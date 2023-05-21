package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import subway.ui.dto.section.SectionCreateRequest;
import subway.ui.dto.section.SectionDeleteRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/section_initialize.sql")
class SectionControllerIntegrationTest {
    private static final Long lineId = 1L;
    private static final String baseUrl = "/lines/{lineId}/sections";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("빈 노선에 구간을 추가하면 추가에 성공한다.")
    void addSection_toEmptyLine_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // expect
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("선릉역"))
                .andExpect(jsonPath("$[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[0].distance").value(50));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 하행역을 추가하면 추가에 성공한다.")
    void addSection_betweenStation_endStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "삼성역", 50);
        addSectionAndCheckReturnValue("역삼역", "선릉역", 20);

        // expect
        mockMvc.perform(get(baseUrl, lineId))
                .andDo(print())
                .andExpect(jsonPath("$[0].startStationName").value("역삼역"))
                .andExpect(jsonPath("$[0].endStationName").value("선릉역"))
                .andExpect(jsonPath("$[0].distance").value(20))
                .andExpect(jsonPath("$[1].startStationName").value("선릉역"))
                .andExpect(jsonPath("$[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].distance").value(30));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 상행역을 추가하면 추가에 성공한다.")
    void addSection_betweenStation_startStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "삼성역", 50);
        addSectionAndCheckReturnValue("강남역", "삼성역", 20);

        // expect
        mockMvc.perform(get(baseUrl, lineId))
                .andDo(print())
                .andExpect(jsonPath("$[0].startStationName").value("역삼역"))
                .andExpect(jsonPath("$[0].endStationName").value("강남역"))
                .andExpect(jsonPath("$[0].distance").value(30))
                .andExpect(jsonPath("$[1].startStationName").value("강남역"))
                .andExpect(jsonPath("$[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].distance").value(20));
    }

    @Test
    @DisplayName("노선의 상행 종점 역 앞에 새로운 역을 추가하면 추가에 성공한다.")
    void addSection_frontOfStartStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("강남역", "역삼역", 50);
        addSectionAndCheckReturnValue("교대역", "강남역", 20);

        // expect
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("교대역"))
                .andExpect(jsonPath("$[0].endStationName").value("강남역"))
                .andExpect(jsonPath("$[0].distance").value(20))
                .andExpect(jsonPath("$[1].startStationName").value("강남역"))
                .andExpect(jsonPath("$[1].endStationName").value("역삼역"))
                .andExpect(jsonPath("$[1].distance").value(50));
    }

    @Test
    @DisplayName("노선의 하행 종점 역 뒤에 역을 추가하면 추가에 성공한다.")
    void addSection_behindEndStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);
        addSectionAndCheckReturnValue("선릉역", "삼성역", 20);

        // expect
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("역삼역"))
                .andExpect(jsonPath("$[0].endStationName").value("선릉역"))
                .andExpect(jsonPath("$[0].distance").value(50))
                .andExpect(jsonPath("$[1].startStationName").value("선릉역"))
                .andExpect(jsonPath("$[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].distance").value(20));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 기준이 되는 역이 없다면 추가에 실패한다.")
    void addSection_betweenStation_noStandard_fail() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("교대역", "강남역", 20);
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 모든 역이 노선에 존재한다면 추가에 실패한다.")
    void addSection_betweenStation_allInLine_fail() throws Exception {
        // given
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("삼성역", "선릉역", 20);
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 51})
    @DisplayName("기존 구간에 역을 추가할 때, 기존 구간보다 길이가 같거나 길다면 추가에 실패한다.")
    void addSection_betweenSection_overExistingSectionDistance(int distance) throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "삼성역", 50);

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("역삼역", "선릉역", distance);
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("노선에 구간을 추가할 때, 구간의 길이가 0이하라면 추가에 실패한다.")
    void addSection_notPositiveDistance_fail(int distance) throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("선릉역", "삼성역", distance);

        // expect
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선에 역을 추가할 때, 존재하지 않는 역이면 추가에 실패한다. ")
    void addSection_notExistsStation_fail() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("장승배기역", "상도역", 10);

        // expect
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("구간의 중간에 있는 역을 삭제할 때, 두 구간이 합쳐진다.")
    void deleteStation_mergeSection_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("선릉역");
        mockMvc.perform(delete(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("역삼역"))
                .andExpect(jsonPath("$[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[0].distance").value(100));
    }

    @ParameterizedTest
    @ValueSource(strings = {"선릉역", "삼성역"})
    @DisplayName("노선에 구간이 1개일 때, 모든 구간이 삭제된다.")
    void deleteStation_singleSection_success(String station) throws Exception {
        // given
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(station);
        mockMvc.perform(delete(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("노선에 구간이 여러 개 일 때, 상행 종점 역을 지울 수 있다.")
    void deleteStation_multipleSection_startStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("역삼역");
        mockMvc.perform(delete(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("선릉역"))
                .andExpect(jsonPath("$[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[0].distance").value(50));
    }

    @Test
    @DisplayName("노선에 구간이 여러 개 일 때, 하행 종점 역을 지울 수 있다.")
    void deleteStation_multipleSection_endStation_success() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // when
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("삼성역");
        mockMvc.perform(delete(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get(baseUrl, lineId))
                .andExpect(jsonPath("$[0].startStationName").value("역삼역"))
                .andExpect(jsonPath("$[0].endStationName").value("선릉역"))
                .andExpect(jsonPath("$[0].distance").value(50));
    }

    @Test
    @DisplayName("노선에 없는 역을 지우면 삭제에 실패한다.")
    void deleteStation_noStation_inLine_fail() throws Exception {
        // given
        addSectionAndCheckReturnValue("역삼역", "선릉역", 50);
        addSectionAndCheckReturnValue("선릉역", "삼성역", 50);

        // expect
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest("교대역");
        mockMvc.perform(delete(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 노선에 대한 요청이 오면 실패한다.")
    void noLineIdFound_fail() throws Exception {
        // given
        mockMvc.perform(get(baseUrl, 3L))
                .andExpect(status().isBadRequest());
    }

    void addSectionAndCheckReturnValue(String startStationName, String endStationName, int distance) throws Exception {
        SectionCreateRequest createRequest = new SectionCreateRequest(startStationName, endStationName, distance);
        mockMvc.perform(post(baseUrl, lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(jsonPath("$.startStationName").value(startStationName))
                .andExpect(jsonPath("$.endStationName").value(endStationName))
                .andExpect(jsonPath("$.distance").value(distance));

    }
}
