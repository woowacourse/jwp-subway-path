# jwp-subway-path

## API SPEC

### 역 추가

- URL: localhost:8080/sections
- method: POST
- Request body

```json
{
  "lineId": 1,
  "leftStationName": "A",
  "rightStationName": "B",
  "distance": "3"
}
```

### 역 제거

- URL: localhost:8080/sections?lineId=1&stationId=1
- method: DELETE

## 테스트 목록

### 노선에 역을 추가 한다.

- [ ] 노선에 역이 존재하지 않는 경우 반드시 2개의 역을 등록해야 한다.
- [ ] 노선에 역이 2개 이상 존재하는 경우
    - [ ] 역의 좌, 우 위치에 역을 등록한다.
        - [ ] 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.

### 노선에 역을 제거한다.

- [ ] 노선에 역이 2개 존재하는 경우 두 역 모두 제거한다.
- [ ] 노선에 역이 2개 이상 존재하는 경우
    - [ ] 역을 제거하고 삭제하는 역의 좌, 우 역을 연결한다.

