package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/section_data_initialize.sql")
class LineControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("모든 노선이 조회되어야 한다.")
    void findAllLines_success() throws Exception {
        // expect
        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("3개의 노선이 조회되었습니다."))
                .andExpect(jsonPath("$.result.size()").value(3));
    }

    @Test
    @DisplayName("역 정보가 포함된 모든 노선이 조회되어야 한다.")
    void findAllDetailLines_success() throws Exception {
        // expect
        mockMvc.perform(get("/lines/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("3개의 상세 노선이 조회되었습니다."))
                .andExpect(jsonPath("$.result[0].line.lineId").value(1))
                .andExpect(jsonPath("$.result[0].line.lineName").value("2호선"))
                .andExpect(jsonPath("$.result[0].line.color").value("bg-green-300"))
                .andExpect(jsonPath("$.result[0].line.extraFee").value(0))
                .andExpect(jsonPath("$.result[0].sections[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[0].sections[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$.result[0].sections[0].distance").value(10))
                .andExpect(jsonPath("$.result[0].sections.size()").value(3))

                .andExpect(jsonPath("$.result[1].line.lineId").value(2))
                .andExpect(jsonPath("$.result[1].line.lineName").value("3호선"))
                .andExpect(jsonPath("$.result[1].line.color").value("bg-orange-300"))
                .andExpect(jsonPath("$.result[1].line.extraFee").value(100))
                .andExpect(jsonPath("$.result[1].sections[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$.result[1].sections[0].endStationName").value("양재역"))
                .andExpect(jsonPath("$.result[1].sections[0].distance").value(10))
                .andExpect(jsonPath("$.result[1].sections.size()").value(4))

                .andExpect(jsonPath("$.result[2].line.lineId").value(3))
                .andExpect(jsonPath("$.result[2].line.lineName").value("4호선"))
                .andExpect(jsonPath("$.result[2].line.color").value("bg-blue-300"))
                .andExpect(jsonPath("$.result[2].line.extraFee").value(200))
                .andExpect(jsonPath("$.result[2].sections[0].startStationName").value("장승배기역"))
                .andExpect(jsonPath("$.result[2].sections[0].endStationName").value("상도역"))
                .andExpect(jsonPath("$.result[2].sections[0].distance").value(10))
                .andExpect(jsonPath("$.result[2].sections.size()").value(5));
    }
}
