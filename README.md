# jwp-subway-path

## 미션 설명
본 미션을 지하철 역과 노선을 관리하는 애플리케이션을 구현하는 것이다.
새로운 역을 추가하고 역을 삭제하고 조회가 가능해야한다.

## 구현할 기능 목록
- [x] 노선에 역 등록 API 신규 규현
  - [x] 첫 2개 등록하는 경우
    - [x] 노선이 존재하는지 파악한다.
    - [x] 요청 온 station이 존재하는지 판단한다.
    - [x] 노선이 새로운 것인지 판단한다.
  - [x] 노선에 역 추가 API 신규 구현
    - [x] 위치는 자유롭게 지정이 가능하다.
    - [x] 역이 등록될 때 거리(양의 정수) 정보도 등록되어야 한다.
    - [x] 하나의 역은 여러 노선이 등록이 가능하다.
    - [x] 역 하나를 종점으로 등록하는 경우
    - [x] 역 하나를 사이에 등록하는 경우
      - [x] 노선 중간에 역이 등록되는 경우 거리 정보를 재계산해야 된다.
      - [x] 노선 중간에 역이 들어오는 경우 세 역 간의 거리의 합과 기존 두역 간의 거리의 합이 같아야 한다.
      - [x] 노선은 갈래길을 가질 수 없다.
- [x] 노선에 역 제거 API 신규 구현
  - [x] 노선에 역이 2개만 있는 경우
    - [x] 역 모두를 삭제한다.
  - [x] 노선에 역이 2개 이상 있는 경우
    - [x] 역 사이의 거리를 재배치한다.
    - [x] 역 간의 위치를 재배치한다.
- [x] 노선 조회 API 수정
  - 노선에 포함된 역을 순서대로 보여주도록 응답한다.
- [x] 노선 목록 조회 API 수정
  - 노선에 포함된 역을 순서대로 보여주도록 응답한다.
- [x] 테이블 설계
  - [x] station
  - [x] line
  - [x] line_station(line과 station의 연결정보)
  - [x] edge(station과 station의 연결정보)

### api 명세
````
post /lines
LineRequest에 정보를 담아 노선을 생성

put /lines/{lineId}
lineId, LineRequest에 정보를 담아 노선을 갱신

post /lines/{id}/init
id, InitStationsRequest에 정보를 담아 노선에 역 두개를 추가해 초기화

post /lines/{id}/stations
id, RegisterStationRequest에 정보를 담아 역을 노선에 등록

get /lines/{id}
id를 기준으로 노선을 조회

get /lines
모든 노선을 조회

delete /lines/{lineId}/stations/{stationId}
lineId의 노선에서 stationId의 역을 제거

delete /lines/{lineId}
lineId의 노선을 제거

post /stations
StationRequest에 정보를 담아 역을 생성

get /stations
모든 역을 조회

get /stations/{id}
id의 역을 조회

put /stations/{id}
id의 역을 StationRequest로 갱신

delete /stations/{id}
id의 역을 제거
````
