# jwp-subway-path

### API 명세

- [x] 노선에 역 추가
  - `/lines/:lineId/sections`
  - POST
  - Request
  ```json
  {
    "startStationName": "잠실나루역",
    "endStationName": "잠실역",
    "distance": 10
  }
  ``` 
  - Response
  ```json
    {
        "id": 1,
        "startStationName": "선릉역",
        "endStationName": "삼성역",
        "distance": 5
    }
  ```


- [x] 노선에 역 제거
  - `lines/:lineId/sections`
  - DELETE
  ```json
  {
    "stationName":"잠실역"
  }
  ```

- [x] 노선 구간 조회
  - `/lines/:lineId/sections`
  - GET
  ```json
  [
    {
        "id": 1,
        "startStationName": "잠실역",
        "endStationName": "삼성역",
        "distance": 10
    },
    {
        "id": 3,
        "startStationName": "삼성역",
        "endStationName": "포항역",
        "distance": 10
    },
    {
        "id": 4,
        "startStationName": "포항역",
        "endStationName": "대구역",
        "distance": 10
    }
  ]
  ```

- [x] 상세 노선 조회
  - `/lines/:lineId`
  - GET
  ```json
  {
    "id": 1,
    "name": "2호선",
    "color": "green",
    "sections": [
      {
        "id": 2,
        "startStationName": "강남역",
        "endStationName": "역삼역",
        "distance": 5
        },
      {
        "id": 1,
        "startStationName": "역삼역",
        "endStationName": "교대역",
        "distance": 1
      }
    ]
  }
  ```
  
- [x] 전체 노선 조회
  - `/lines`
  - GET
  ```json
  [
      {
          "id": 1,
          "name": "2호선",
          "color": "green",
          "sections": [
              {
                  "id": 2,
                  "startStationName": "강남역",
                  "endStationName": "역삼역",
                  "distance": 5
              },
              {
                  "id": 1,
                  "startStationName": "역삼역",
                  "endStationName": "교대역",
                  "distance": 1
              }
          ]
      },
      {
          "id": 2,
          "name": "3호선",
          "color": "orange",
          "sections": [
              {
                  "id": 4,
                  "startStationName": "삼성역",
                  "endStationName": "역삼역",
                  "distance": 5
              },
              {
                  "id": 5,
                  "startStationName": "역삼역",
                  "endStationName": "교대역",
                  "distance": 1
              }
          ]
      }
  ]
  ```

- [ ] 경로 조회 API 구현
  - `/paths`
  - GET
  - Request
  ```json
  {
    "sourceStationName":"강남역",
    "destStationName":"양재역"
  }
  ```

  - Response
  ```json
  {
    "pathStations": [
      {
          "id":1,
          "name": "강남역"
      },
      {
          "id":2,
          "name": "교대역"
      }
    ],
    "distance":20,
    "fare":2250
  }
  ```

### 프로덕션 데이터베이스 docker 실행
```text
# 루트 디렉터리에서 실행 (jwp-subway-path)
cd docker
docker-compose -p subway up -d
```

### 기능 요구 사항

- [x] 노선에 역 등록
  - [x] 노선에 역을 등록할 때는 상행, 하행 상관 없이 등록 할 수 있다.
    - [x] 없는 노선에 역을 추가할 경우, 예외가 발생한다.
    - [x] 없는 역을 입력할 경우, 예외가 발생한다.
  - [x] startStation이 상행역이고, endStation이 하행역이다.
    - [x] 노선에 구간이 없으면 바로 추가된다.
    - [x] startStation과 endStation 둘 다 노선에 있거나, 없으면 예외가 발생해야 한다. 
    - [x] 노선에 역을 추가할 때, 두 역 중 반드시 하나만 해당 노선에 있어야 한다.
    - startStation가 이미 노선에 있는 역이라면, endStation가 startStation 기준 하행역으로 추가된다.
    - endStation가 이미 노선에 있는 역이라면, startStation가 endStation 기준 상행역으로 추가된다.
  - [x] 추가되는 구간의 길이가 기존 구간의 길이보다 작아야 한다.
  - [x] 구간의 길이는 0보다 커야 한다.

- [x] 노선에 역 삭제
  - [x] 노선에서 역을 삭제할 때는, 노선이 재배치 되어야 한다.
    - [x] A-B-C-D 역이 있는 노선에서 C를 삭제하면 A-B-D 순으로 재배치 된다.
    - [x] A-2km-B, B-3km-C, C-4km-D 일 경우, 구간의 길이가 B-7km-D가 되어야 한다.
  - [x] 노선에 등록된 역이 2개일때, 하나의 역을 삭제하면 두 역이 삭제된다.

- [x] 데이터베이스 분리
  - [x] 프로덕션 환경에서는 로컬 데이터베이스를 사용하도록 설정한다.
  - [x] 테스트 환경에서는 인메모리 데이터베이스를 사용하도록 설정한다. 

- [x] 경로 조회
  - [x] 최단 경로를 조회할 수 있는 그래프를 생성한다.
  - [x] 노선에 상관없이 시작역 - 도착역 사이의 최단 경로를 조회한다.
    - [x] 요청한 시작역과 도착역이 모두 노선에 있지 않다면 예외가 발생한다.
    - [x] 요청한 시작역에서 도착역으로 갈 수 있는 경로가 없다면 예외가 발생한다.
  - [x] 찾은 최단 경로의 총 거리 정보, 모든 경로 정보를 반환한다. 
  - [x] 찾은 경로의 총 이동 거리에 따른 요금을 계산한다.

- 요금 조회 기능
  - 10km 이하 (기본운임): 1,250원
  - 10km~50km: 5km마다 100원 추가
    - 10km 초과 ~ 15km 이하: 1250 + 100
    - 15km 초과 ~ 20km 이하: 1250 + 200
  - 50km 초과: 8km 마다 100원 추가
    - 58km: 1250(10km) + 800(40km → 8*100원) + 100(8km → 100원) ⇒ 2250원