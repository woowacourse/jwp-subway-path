# jwp-subway-path

### API 명세서

| Method | URL                     | Description |
|--------|-------------------------|-------------|
| GET    | `/lines`                | 모든 노선 조회    |
| POST   | `/lines`                | 새 노선 생성     |
| GET    | `/lines/{lindId}`       | 노선 조회       |
| DELETE | `/lines/{lineId}`       | 노선 제거       |
| POST   | `/stations`             | 새 역 등록      |
| DELETE | `/stations/{stationId}` | 역 제거        |
| POST   | `/paths`                | 최단 경로 조회    |


