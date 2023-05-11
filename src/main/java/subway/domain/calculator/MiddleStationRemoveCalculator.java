package subway.domain.calculator;

import subway.domain.section.SectionEntities;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.Optional;

public class MiddleStationRemoveCalculator {

    private final SectionEntities sectionEntities;

    public MiddleStationRemoveCalculator(SectionEntities sectionEntities) {
        this.sectionEntities = sectionEntities;
    }

    /**
     * ?? section이 새로 생기니까 반환할 수 있나
     * <p>
     * section 테이블에서 stationId로 찾은 역 목록 개수가 2개 미만이면 예외를 던진다.
     * section 테이블에서 stationId로 찾은 역 목록 개수가 2개면 아래의 상황이다.
     * a - b 에서 a 제거
     * <p>
     * line 자체를 제거시키기만 하면 됨 (CASCADE delete) return
     * <p>
     * upSection 제거하려고 하는 역이 상행역인 section
     * downSection 제거하려고 하는 역이 하행역인 section
     * <p>
     * <p>
     * upSection.isEmpty() && !downSection.isEmpty() -> 상행 종점
     * !upSection.isEmpty() && downSection.isEmpty() -> 하행 종점
     * !upSection.isEmpty() && !downSection.isEmpty() - > 중간 역을 제거하는 경우
     * <p>
     * <p>
     * 중간 역을 제거하는 경우
     * a - b - c 순인데 b를 삭제하면 a - c 연결시켜줘야 함.
     * (b를 기준으로 상행역 하행역 정보를 알아야 함)
     * section에서 a - b를 찾아서 제거하기 전에 a 정보와 거리를 알아야 함 -> 제거
     * section에서 b - c를 찾아서 제거하기 전에 c 정보와 거리를 알아야 함 -> 제거
     * section에 a - c 를 만들어서 넣어 줌 (거리 더해서)
     * <p>
     * station 테이블에서 제거
     * <p>
     * -----------------------------
     * 종점을 제거하는 경우
     * 상행 종점을 제거하는 경우
     * a - b - c 에서 a 제거
     * a가 상행역인 section을 제거
     * <p>
     * 하행 종점을 제거하는 경우
     * a - b - c 에서 c 제거
     * c가 하행역인 section을 제거
     */

    // 중간역 제거 시에만 호출
    public SectionEntity calculateSectionToAdd(StationEntity stationEntity) {
        Optional<SectionEntity> nullableUpSection = sectionEntities.findUpSectionByStation(stationEntity);
        Optional<SectionEntity> nullableDownSection = sectionEntities.findDownSectionByStation(stationEntity);

        SectionEntity upSection = nullableUpSection.get();
        SectionEntity downSection = nullableDownSection.get();
        int upDistance = upSection.getDistance();
        int downDistance = downSection.getDistance();
        int newDistance = upDistance + downDistance;

        Long newUpStationId = upSection.getUpStationId();
        Long newDownStationId = downSection.getDownStationId();

        return new SectionEntity(null, newUpStationId, newDownStationId, stationEntity.getLineId(), newDistance);
    }
}
