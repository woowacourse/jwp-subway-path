# jwp-subway-path

## 1단계 기능 목록

- [ ] 역, 노선 CRUD API 작성
    - [x] 새 노선 (역 2개) 등록
    - [x] 새 역 등록

| Method | URL                     | Description    |
|--------|-------------------------|----------------|
| POST   | `/lines`                | 새 노선 (역 2개) 등록 |
| POST   | `/stations`             | 새 역 등록         |
| DELETE | `/lines/{lineId}`       | 노선 (역 2개) 제거   |
| DELETE | `/stations/{stationId}` | 역 제거           |

