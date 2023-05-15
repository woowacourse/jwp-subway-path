# jwp-subway-path

### API 명세서

| Method | URL                     | Description    |
|--------|-------------------------|----------------|
| GET    | `/lines`                | 모든 노선 조회       |
| POST   | `/lines`                | 새 노선 (역 2개) 등록 |
| GET    | `/lines/{lindId}`       | 노선 조회          |
| DELETE | `/lines/{lineId}`       | 노선 제거          |
| POST   | `/stations`             | 새 역 등록         |
| DELETE | `/stations/{stationId}` | 역 제거           |


