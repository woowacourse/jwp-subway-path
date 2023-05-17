# jwp-subway-path

### 1단계 리팩터링 목록

- [x] README 문서 업데이트
- [x] [`IllegalArgumentException`에 대한 처리 400대 VS 500대](https://github.com/woowacourse/jwp-subway-path/pull/35#discussion_r1193146265)
- [ ] domain Line이 Station과의 관계를 드러낼 수 있도록 수정하기 => 계층적 구조 드러내기
    - Line이 Sections를 가지고, Sections는 Graph만 가지도록 수정하려고 했지만.... 너무 대 공사가 되어서, 이번 리뷰요청에서 하지 못했어요ㅠㅠ
- [x] 도메인 내 주석 없애기 => 최대한 코드만으로 표현하자!
- [x] Sections 중복 코드 없애기
- [ ] MockMVC 사용한 테스트 코드 작성


# API 설계

| HttpMethod | URL                                  | HttpStatus | Description         |
|------------|--------------------------------------|------------|---------------------|
| GET        | /lines                               | 200        | 전체 노선 목록을 조회한다.     |
| GET        | /lines/{lineId}                      | 200        | 특정 노선을 조회한다.        |
| POST       | /lines                               | 201        | 노선을 생성한다.           |
| POST       | /lines/{lineId}/stations             | 201        | 특정 노선에 역을 추가한다.     |
| POST       | /lines/{lineId}/station-init         | 201        | 노선에 최초로 역 2개를 추가한다. |
| DELETE     | /lines/{lineId}/stations/{stationId} | 204        | 특정 노선에서 특정 역을 삭제한다. |
| POST       | /stations                            | 201        | 역을 생성한다.            |

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
