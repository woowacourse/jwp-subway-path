# jwp-subway-path

|                   | METHOD | URI                 | BODY                 | 설명                                                                                                      |
|-------------------|--------|---------------------|----------------------|---------------------------------------------------------------------------------------------------------|
| 노선 새로 만들기         | POST   | /lines              | LineRequest          | 주어진 노선 이름과 색상 정보를 바탕으로 노선을 저장합니다.                                                                       |
| 모든 노선 찾기          | GET    | /lines              |                      | 등록된 모든 노선 정보를 조회합니다. 조회한 각 노선은 이름, 색상, 노선에 등록된 역 목록을 상행부터 정렬해 보여줍니다.                                    |
| 아이디로 노선 찾기        | GET    | /lines/{id}         |                      | 등록된 ID에 해당하는 노선 정보를 조회합니다. 조회한 노선은 이름, 색상, 노선에 등록된 역 목록을 상행부터 정렬해 보여줍니다.                                |
| 노선 정보 수정하기        | PUT    | /lines/{id}         | LineRequest          | 주어진 ID에 해당하는 노선 정보를 수정합니다.                                                                              |
| 노선 삭제하기           | DELETE | /lines/{id}         |                      | 주어진 ID에 해당하는 노선 정보를 삭제합니다.                                                                              |
|                   |        |                     |                      |                                                                                                         |
| 구간 새로 만들기         | POST   | /lines/{id}/section | SectionSavingRequest | 주어진 ID에 해당하는 노선 위에 구간을 추가합니다. 주어진 정보로는 추가할 역 이름, 연결할 역 이름, 거리 그리고 추가할 역이 연결할 역에 상행방향으로 붙어있는 지 여부가 있습니다. |
| 구간 사이 최단 도착정보 구하기 | GET    | /lines/section      |                      |                                                                                                         |
| 구간 삭제하기           | DELETE | /lines/{id}/section | StationRequest       | 주어진 ID에 해당하는 노선 위에 있는 역을 삭제합니다. 주어진 정보로는 삭제할 역 이름이 있습니다.                                                |
|                   |        |                     |                      |                                                                                                         |
| 역 새로 만들기          | POST   | /stations           | StationRequest       | 새로운 역 정보를 저장합니다. 주어진 정보로는 역 이름이 있습니다.                                                                   |
| 모든 역 찾기           | GET    | /stations           |                      | 저장되어 있는 모든 역 정보를 조회합니다.                                                                                 |
| 아이디로 역 찾기         | GET    | /stations/{id}      |                      | 주어진 ID에 해당하는 역 정보를 조회합니다.                                                                               |
| 역 정보 수정하기         | PUT    | /stations/{id}      | StationRequest       | 주어진 ID에 해당하는 역 정보를 수정합니다.                                                                               |
| 역 정보 삭제하기         | DELETE | /station/{id}       |                      | 주어진 ID에 해당하는 역 정보를 삭제합니다.                                                                               |

