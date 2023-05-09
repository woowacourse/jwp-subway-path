# jwp-subway-path

# 🤔다시 만난 페어

<table>
    <tr>
        <td align="center"><img src="https://avatars.githubusercontent.com/u/79090478?v=4" width=500></td>
        <td align="center"><img src="https://avatars.githubusercontent.com/u/82203978?v=4" width=500></td>
    </tr>
    <tr>
        <td align="center"><a href="">루카</a></td>
        <td align="center"><a href="">헤나</a></td>
    </tr>
</table>

# 시나리오

- 역 등록
    - 기존 노선이 없을 경우
        - A, B와 새로운 노선을 함께 등록한다.
    - 기존 노선이 있을 경우
        - 새로운 역 C를 등록할 경우
            - C가 기존 노선일 경우
                - 등록한다.
            - C가 기존 노선이 아닐 경우
                - C의 노선이 없을 경우
                    - 새로운 노선을 만들고, 등록한다.
                - C의 노선이 있을 경우
                    - 등록한다.

---

# 요구 사항

## 역

- [ ] 역을 등록한다.
- [ ] 역을 삭제한다.

## 구간

---

# API

## POST /apis/v1/stations

```http
POST  /apis/v1/stations

{
  "up": "잠실새내",
  "down": "잠실",
  "호선": 8,
  "거리": 10
}
```

```http request
201 CREATED
location: /apis/v1/stations/1
```
