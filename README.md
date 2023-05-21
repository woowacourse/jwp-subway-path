# jwp-subway-path

## 기능 요구 사항

### Domain

- [x] Line (1호선, 2호선, ...)
    - LineInfo
    - Sections(호선에 존재하는 역들의 구간 정보)
- [x] LineInfo
    - 호선 이름
        - 호선 이름은 중복될 수 없다.
        - 호선 이름은 1글자 이상, 10글자 이하여야 한다.
- [x] Sections
    - 상행 종점
    - 하행 종점
    - Section이 중복될 수 없다.
        - StartStation과 EndStation이 같은 두 개의 Section을 가질 수 없다.
        - 역이 등록될 경우 거리 정보를 고려해야 한다.
            - A역 (3km) B역 인 경우, A역 (10km) C역 추가 시 예외
            - A역 (3km) B역 인 경우, A역 (1km) C역 추가 시, A역 (1km) C역, C역 (2km) B역 이 된다.
        - 역이 삭제될 경우 거리 정보를 고려해야 한다.
            - A역 (1km) C역, C역 (2km) B역에서 C역을 삭제하는 경우,
                - A역 (3km) B역이 된다.
                - A역 - C역 Section과 C역 - B역 Section이 삭제 된다.
            - 역이 두 개일 때, 삭제할 경우 두 역이 모두 제거된다.
    - 새로 등록, 삭제되는 노선에 따라, 종점이 변경될 수 있다.
- [x] Section
    - StartStation
    - EndStation
        - 시작 역과 도착 역이 동일할 수 없다.
        - 두 개의 역을 반드시 등록해야 한다.
    - Distance
- [x] Station 역
    - 이름
        - 역 이름은 1글자 이상, 10글자 이하여야 한다.
- [x] Distance 역간거리
    - 거리
        - 양의 정수만 가능하다.
- [X] Cost 요금
  - 기본 운임(10km 이내)는 1250원이다.
  - 이동거리가 10km ~ 50km인 경우, 5km마다 100원이 추가된다.
  - 이동거리가 50km를 초과하는 경우, 8km마다 100원이 추가된다.

### API 명세

- [X] 노선에 호선 등록

```text
POST /subway/lines

Request {
    lineName : "1호선"
}

Response Headers {
    Status : 201
    Location : /subway/lines/{line_id}
}
```

- [X] 노선에 역 구간 등록

```text
POST /subway/lines/{line_id}/sections

Request {
    startStation : "회기",
    endStation : "청량리",
    distance : 3
}

Response Headers {
    Status : 200
}
```

- [X] 노선에 역 제거

```text
DELETE /subway/lines/{lineId}/stations/{station_id}

Response Headers {
    Status : 204
}
```

- [X] 노선 상세 조회

```text
GET /subway/lines/{line_id}

Response Headers {
    Status : 200
}

Response Body {
    id : 1L,
    lineName : "1호선",
    stations : [
         {
            id : 1L,
            name : "회기"
        },
        {
            id : 2L,
            name : "청량리"
        }
   ]
}
```

- [X] 노선 목록 조회

```text
GET /subway/lines

Response Headers {
    Status : 200
}

Response Body {
    [
        {
            id : 1L,
            lineName : "1호선",
            stations : [
                 {
                    id : 1L,
                    name : "회기"
                },
                {
                    id : 2L,
                    name : "청량리"
                }
            ]
        },
       {
            id : 2L,
            lineName : "2호선",
            stations : [
                 {
                    id : 3L,
                    name : "잠실"
                },
                {
                    id : 4L,
                    name : "잠실새내"
                }
            ] 
       }
    ]
}
```

- [X] 경로 조회 
```text
GET /subway/path/{start_station}/{arrival_station}

Response Headers {
    Status : 200
}

Response Body {
    stations : [
        {
            id : 1L,
            name : "회기"
        },
        {
            id : 2L,
            name : "청량리"
        }
    ],
    cost : 1250
}

```
