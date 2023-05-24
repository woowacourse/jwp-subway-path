# jwp-subway-path

# 기능요구사항

- [x] 노선에 역 등록 API 구현
  - [x] 노선 정보, 역 두 개, 역 사이의 거리를 입력 받아야한다.
  - [x] 노선에 등록되는 역의 위치는 자유롭게 지정할 수 있어야 한다.
  - [x] 하나의 역은 여러 노선에 등록이 될 수 있다.(환승)
  - [x] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 한다.
    - [x] 역 사이에 역을 등록할 때 거리가 조절되어야 한다.
    - [x] 역 사이에 역을 등록할 때 기존 역 사이의 거리보다 크거나 같아서는 안된다.
- [x] 노선에 역 제거 API 구현
  - [x] 역 사이의 역을 삭제할 때 거리랑 역의 순서가 재배치 되어야 한다.
  - [x] 등록된 역이 두 개인 경우에 하나를 제거하면 모두 제거되어야 한다.
- [x] 노선 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
```json
{
  "id": 1L,
  "name": "노선이름",
  "color": "노선색깔",
  "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
}
```
- [x] 노선 목록 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
```json
[
  {
    "id": 1L,
    "name": "노선이름1",
    "color": "노선색깔1",
    "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
  },
  {
    "id": 2L,
    "name": "노선이름2",
    "color": "노선색깔2",
    "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
  }
]
```
- [x] 노선을 최초 등록 할 때는 두 개의 역을 등록해야 한다.
- [x] Jgrapht 라이브러리 의존성을 추가한다.
- [x] production과 test DB환경을 분리한다.
- [x] 경로 조회 API를 구현한다.
  - [x] 경로 정보를 반환한다.
  - [x] 총 거리를 반환한다.
  - [x] 요금을 반환한다.
    - [x] 요금 계산 방법
      - 기본운임(10㎞ 이내): 기본운임 1,250원
      - 이용 거리 초과 시 추가운임 부과
      - 10km~50km: 5km 까지 마다 100원 추가
      - 50km 초과: 8km 까지 마다 100원 추가
```json
{
  "route": [ {"id": 1L, "name": "잠실역"}, {"id": 2L, "name": "잠실새내역"}],
  "totalDistance": 20,
  "fare": 1550
}
```

## 도메인 설계

- Line(노선)
  - id
  - Name name
  - Color color

- SingleLineSections
  - List<Section> sections 

- MultiLineSections
  - List<Section> sections 
 
- Section(구간)
  - Station downStation
  - Station upStation
  - Long lineId
  - Distance distance

- Station(역)
  - id ID
  - Name 이름

- Fare(요금)
  - int fare

- ShortestPath(최단거리)
  - List<Station> stations
  - Distance distance

## API 문서
- [API 구현 목록](src/main/resources/static/docs)
