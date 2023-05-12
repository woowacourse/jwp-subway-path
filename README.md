# jwp-subway-path

# API 설계

| HttpMethod | URL                                  | HttpStatus | Description         |
|------------|--------------------------------------|------------|---------------------|
| GET        | /lines                               | 200        | 전체 노선 목록을 조회한다.     |
| GET        | /lines/{lineId}                      | 200        | 특정 노선을 조회한다.        |
| POST       | /lines                               | 201        | 노선을 생성한다.           |
| DELETE     | /lines/{lineId}                      | 204        | 특정 노선을 삭제한다.        |
| POST       | /lines/{lineId}/stations             | 201        | 특정 노선에 역을 추가한다.     |
| DELETE     | /lines/{lineId}/stations/{stationId} | 204        | 특정 노선에서 특정 역을 삭제한다. |

## POST /lines

#### Request

BODY

```json
{
  "name": "2호선",
}
```

#### Response

BODY

```json
{
  "id": 1,
  "name": "2호선"
}
```
