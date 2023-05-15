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

## 지하철 2단계 요구사항

- [ ] 요금 조회 기능 추가
- [ ] 경로 조회 API 구현
  - 출발역과 도착역 사이의 최단 거리 경로를 구하는 API를 구현합니다.
  - 최단 거리 경로와 함께 총 거리 정보를 함께 응답합니다.
  - 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려합니다.


- 요금 조회 기능 추가
  - [ ] 경로 조회 시 요금 정보를 포함하여 응답합니다.

- 요금 계산 방법
  - 기본운임(10㎞ 이내): 기본운임 1,250원
  - 이용 거리 초과 시 추가운임 부과
  - 10km~50km: 5km 까지 마다 100원 추가
  - 50km 초과: 8km 까지 마다 100원 추가
