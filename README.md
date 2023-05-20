# 지하철 미션

## RESP API

### 노선에 역 등록 ( 구간 등록 )

노선에 새로운 역을 등록합니다. 기준역에서 상행 혹은 하행 방향으로 새로운 역을 등록합니다. 빈 노선일 경우 기준역과 새로운 역을 모두 등록합니다.

#### Request

```
curl -X POST "https://localhost:8080/lines/${lineId}/sections" \
    --header "Content-Type: application/json" \
    --data "baseStation": "기준역" \
    --data "newStation": "추가역" \
    --data "direction": "상행" \
    --data "distance": 3 
```

#### Request Parameter

| Name        | Type    | Description                | Required |
|-------------|---------|----------------------------|----------|
| baseStation | String  | 기준이 되는 역의 이름               | O        |
| newStation  | String  | 새로 등록할 역의 이름               | O        |
| direction   | String  | 기준역으로 부터 등록할 방향 ( 상행, 하행 ) | O        |
| distance    | Integer | 기준역부터 등록할 역까지의 거리          | O        |

#### Response

성공

```
HTTP/1.1 201 Created
Location : “lines/${lineId}/sections/{sectionId}”
```

실패

1. 역이 들어가야할 구간의 길이가 충분히 길지 않을 때

```
HTTP/1.1 400 Bad Request
Content-Type: application/json;

{
    "message": "역이 들어가야할 구간의 길이가 충분하지 않습니다.(역 추가에 필요한 길이: ${생성되어야 할 길이}, 현재 구간의 길이 : ${현재 구간의 길이})"
}
```

2. 기준이 되는 역이 노선에 존재하지 않을 때

```
HTTP/1.1 400 Bad Request
Content-Type: application/json;

{
    "message": "역이 노선에 존재하지 않습니다 ( 존재하지 않는 역: {역 이름} )"
}
```

---

### 노선에 역 제거 ( 구간 제거 )

노선에 있는 역을 제거합니다.

#### Request

```
curl -X DELETE "https://localhost:8080/lines/${lineId}/sections?stationName=${delete_station_name}"
```

#### Request Parameter

| Name        | Type   | Description | Required |
|-------------|--------|-------------|----------|
| lineId      | Long   | 노선의 ID      | O        |
| stationName | String | 삭제하려는 역의 이름 | O        |

#### Response

성공

```
HTTP/1.1 204 No Content
```

실패

1. 제거하려는 역이 존재하지 않을 경우

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;

{
  "message": "역이 노선에 존재하지 않습니다 ( 존재하지 않는 역: {역 이름} )"
}
```

---

### 노선 조회 개선

기존 : 노선의 Id, name, color 조회합니다.

개선 : 노선의 id, name, color, 포함된 역(순서대로 정렬) 조회합니다.

#### Request

```

curl -X GET "https://localhost:8080/lines/${lineId}"

```

#### Request Parameter

| Name   | Type | Description  | Required |
|--------|------|--------------|----------|
| lineId | Long | 조회하려는 노선의 ID | O        |

#### Response

성공

```
HTTP/1.1 OK
Content-Type: application/json;
{
    "id": 1,
    "name": "노선 이름",
    "color": "노선 색상",
    "additionalFee": 100
    "stations" : [
        {
            "id": 1,
            "name": "첫번째역 이름"
        },
        {
            "id": 2,
            "name": "두번째역 이름"
        },
        ...
    ]
}
```

실패

- ${lineId}에 해당하는 노선이 존재하지 않을 때

```

HTTP/1.1 Bad Request
Content-Type: application/json;
{
    "message" : "노선이 존재하지 않습니다."
}

```

#### Response Parameter

| Name          | Type      | Description    | Required |
|---------------|-----------|----------------|----------|
| id            | Long      | 노선의 ID         | O        |
| name          | String    | 노선의 이름         | O        |
| color         | String    | 노선의 색상         | O        |
| additionalFee | Integer   | 노선 사용시 추가요금    | O        |
| stations      | Station[] | 노선에 등록된 역들의 목록 | O        |

Station

| Name | Type   | Description | Required |
|------|--------|-------------|----------|
| id   | Long   | 역의 ID       | O        |
| name | String | 역의 이름       | O        |

---

### 모든 노선 조회 개선

기존 : 모든 노선의 Id, name, color 조회합니다.

개선 : 모든 노선의 id, name, color, 포함된 역(순서대로 정렬) 조회합니다.

#### Request

```

curl -X GET "https://localhost:8080/lines"

```

#### Response

```
HTTP/1.1 OK
Content-Type: application/json;
{
    "lines" : [
        {
            "id": 1,
            "name": "노선 이름",
            "color": "노선 색상",
            "additionalFee": 100,
            "stations" : [
                {
                    "id": 1,
                    "name": "첫번째역 이름"
                },
                {
                    "id": 2,
                    "name": "두번째역 이름"
                },
                ...
            ]
        },
        {
            "id": 2,
            "name": "노선 이름",
            "color": "노선 색상",
            "additionalFee": 200,
            "stations" : [
                {
                    "id": 3,
                    "name": "첫번째역 이름"
                },
                {
                    "id": 4,
                    "name": "두번째역 이름"
                },
                ...
            ]
        },
        ...
    ]
}
```

#### Response Parameter

| Name  | Type   | Description | Required |
|-------|--------|-------------|----------|
| lines | Line[] | 모든 노선의 목록   | O        |

Line

| Name          | Type      | Description    | Required |
|---------------|-----------|----------------|----------|
| id            | Long      | 노선의 ID         | O        |
| name          | String    | 노선의 이름         | O        |
| color         | String    | 노선의 색상         | O        |
| additionalFee | Integer   | 노선 사용시 추가요금    | O        |
| stations      | Station[] | 노선에 등록된 역들의 목록 | O        |

Station

| Name | Type   | Description | Required |
|------|--------|-------------|----------|
| id   | Long   | 역의 ID       | O        |
| name | String | 역의 이름       | O        |

---

### 경로 조회

시작역부터 도착역까지의 최단 거리의 경로를 총 거리와 요금과 함께 반환합니다.

#### Request

```

curl -X GET "https://localhost:8080/subway?start={start}&end={end}&age={passenger_age}"

```

#### Request Parameter

| Name  | Type    | Description | Required |
|-------|---------|-------------|----------|
| start | String  | 시작역의 이름     | O        |
| end   | String  | 도착역의 이름     | O        |
| age   | Integer | 승객의 나이      | O        |

#### Response

```

HTTP/1.1 200 OK
Content-Type: application/json;
{
    "route":[
        {
            "stationId": 1,
            "name": "시작역",
            "lineId": 2,
            "lineName": "시작역 노선",
            "lineColor" : "시작역 노선 색상"
        },
        {
            "stationId": 7,
            "name": "다음역",
            "lineId": 3,
            "lineName": "다음역 노선",
            "lineColor" : "다음역 노선 색상"
        },
        ...
        {
            "stationId": 3,
            "name": "도착역",
            "lineId": 2,
            "lineName": "도착역 노선",
            "lineColor" : "도착역 노선 색상"
        }
    ],
    "totalDistance": 15,
    "totalBudget": 3400
}

```

#### Response Parameter

| Name          | Type          | Description | Required |
|---------------|---------------|-------------|----------|
| route         | LineStation[] | 경로에 존재하는 역  | O        |
| totalDistance | Integer       | 총 거리        | O        |
| totalBudget   | Integer       | 총 요금        | O        |

LineStation

| Name      | Type   | Description | Required |
|-----------|--------|-------------|----------|
| stationId | Long   | 역의 ID       | O        |
| name      | String | 역의 이름       | O        |
| lineId    | Long   | 노선의 ID      | O        |
| lineName  | String | 노선의 이름      | O        |
| lineColor | String | 노선의 색상      | O        |

---

## 비즈니스 요구사항

### 노선에 역 등록

-[x] 아래 정보를 통해 노선에 역을 등록한다.
    -[x] 기준역의 이름
    -[x] 등록역의 이름
    -[x] 등록할 방향
    -[x] 두 역사이의 거리
-[x] 노선이 비어있을 때 역을 등록한다.
    -[x] 등록할 방향으로 기준역과 등록역 모두 생성된다.
-[x] 노선의 상행 종점의 상행방향과 하행 종점의 하행방향에 역을 등록한다.
    -[x] 기준역이 노선에 없을 경우 예외를 발생시킨다.
    -[x] 상행 종점의 상행방향에 역을 등록할 경우 추가된 역이 노선의 상행 종점이 된다.
    -[x] 하행 종점의 하행방향에 역을 등록할 경우 추가된 역이 노선의 하행 종점이 된다.
-[x] 노선의 기존 구간 사이에 역을 등록한다. ( 전체 노선의 길이는 변하지 않는다. )
    -[x] 기준역이 노선에 없을 경우 예외를 발생시킨다.
    -[x] 상행역을 기준으로 하행역을 추가한다 .
        -[x] 기존의 상행역에서 추가역으로 하행하는 구간을 생성한다.
        -[x] 추가역에서 기존의 하행역으로 하행하는 구간을 생성한다.
        -[x] 기존 구간을 삭제된다.
    -[x] 하행역을 기준으로 상행역을 추가한다. ( 전체 노선의 길이는 변하지 않는다. )
        -[x] 기존 구간의 하행역에서 추가역으로 상행하는 구간을 생성한다.
        -[x] 추가역에서 기존 구간의 상행역으로 하행하는 구간을 생성한다.
        -[x] 기존 구간을 삭제한다.
    -[x] 기존 구간의 길이가 역이 등록되며 생기는 구간들보다 길지 않으면 예외를 발생시킨다.

### 노선에서 역 제거

-[x] 노선에서 이름이 동일한 역을 제거한다.
-[x] 이름이 동일한 역이 노선에 없을 경우 예외를 발생시킨다.
-[x] 노선에 역이 두개일 경우 모든 구간을 삭제한다.
-[x] 노선의 종점을 삭제한다.
    -[x] 해당 역이 포함된 구간 1개를 삭제한다.
-[x] 노선의 중간에 있는 역을 삭제한다.
    -[x] 해당 역을 하행역으로 가진 구간의 상행역과 해당 역을 상행역으로 가진 구간의 하행역을 연결한 구간의 생성한다.
    -[x] 해당 역을 상행역으로 가진 구간을 삭제한다.
    -[x] 해당 역을 하행역으로 가진 구간을 삭제한다.

### 노선의 역 순서대로 조회

-[x] 노선의 모든 구간을 순서대로 정렬하여 반환한다.

### 운임 요금

#### 기본 운임 요금 정책

-[x] 기본운임(10㎞ 이내): 기본운임 1,250원
-[x] 이용 거리 초과 시 추가운임 부과
    -[x] 10km~50km: 5km 까지 마다 100원 추가
    -[x] 50km 초과: 8km 까지 마다 100원 추가

#### 노선별 추가 요금 정책

-[x] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가합니다.
-[x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용합니다.

#### 연령별 요금 할인 정책

-[x] 청소년 (13세 이상~19세 미만) : 운임에서 350원을 공제한 금액의 20%할인
-[x] 어린이 (6세 이상~13세 미만): 운임에서 350원을 공제한 금액의 50%할인
-[x] 아기 (6세 미만 : 운임료를 받지 않는다
