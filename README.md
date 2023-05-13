# jwp-subway-path

# API 설계

| HttpMethod | URL                                  | HttpStatus | Description         |
|------------|--------------------------------------|------------|---------------------|
| GET        | /lines                               | 200        | 전체 노선 목록을 조회한다.     |
| GET        | /lines/{lineId}                      | 200        | 특정 노선을 조회한다.        |
| POST       | /lines                               | 201        | 노선을 생성한다.           |
| POST       | /lines/{lineId}/stations             | 201        | 특정 노선에 역을 추가한다.     |
| DELETE     | /lines/{lineId}/stations/{stationId} | 204        | 특정 노선에서 특정 역을 삭제한다. |

## POST /lines

#### Request

BODY

```json
{
  "name": "2호선",
  "color": "GREEN"
}
```

#### Response

BODY

```json
{
  "id": 1,
  "name": "2호선",
  "color": "GREEN"
  
}
```

### POST /lines/{lineId}/stations

#### Request

Body

```json
{
  "upStation": 1,
  "downStation": 2,
  "distance": 15
}
```

#### Response

Body

```json
{
  "id": 1,
  "name": "2호선",
  "color": "GREEN",
  "stations": [
    {
      "id": 1,
      "name": "역삼역"
    },
    {
      "id": 2,
      "name": "삼성역"
    },
    {
      "id": 3,
      "name": "잠실역"
    }
  ]
}
```
