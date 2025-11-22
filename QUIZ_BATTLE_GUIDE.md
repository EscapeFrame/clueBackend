# Quiz Battle - 실시간 퀴즈 배틀 시스템 가이드

Socket(WebSocket)을 이용한 Kahoot 스타일의 실시간 퀴즈 배틀 서비스입니다.

## 주요 기능

### ✅ 1. 방 생성
- 호스트가 퀴즈 방을 생성
- 고유한 6자리 방 코드 자동 생성
- 주제, 문제 수, 제한 시간, 최대 참가자 수 설정
- **방 생성 시 FastAPI로 문제 미리 생성 (RAG 기반)**
- documentId를 통한 특정 문서 기반 문제 생성 지원

### ✅ 2. 방 참여
- 방 코드로 입장
- 실시간으로 참가자 목록 업데이트
- 최대 참가자 수 제한

### ✅ 3. 문제 풀기
- RAG 기반 자동 문제 생성 (FastAPI 연동)
- 정답 여부에 따른 점수 부여
- 빠른 답변에 대한 시간 보너스 (최대 50점)

### ✅ 4. 자동 문제 전환
- **각 문제의 제한 시간이 끝나면 자동으로 다음 문제로 이동**
- **호스트가 수동으로 다음 문제로 넘기기 가능**
- 타이머 자동 취소 및 관리

### ✅ 5. 실시간 랭킹
- 점수 기반 실시간 랭킹 계산
- 정답률, 평균 응답 시간 등 통계 제공

## 기술 스택

```
Backend:
- Spring Boot 3.4.4 + WebSocket (STOMP)
- PostgreSQL (방 정보 영구 저장)
- Redis (실시간 상태 관리)
- OpenFeign (FastAPI 통신)

AI:
- FastAPI (RAG 기반 문제 생성)
```

## WebSocket 엔드포인트

### 연결
```
ws://localhost:8080/ws-quiz
```

### 클라이언트 → 서버 (발신)

| 엔드포인트 | 설명 | 권한 |
|-----------|------|------|
| `/app/quiz/create` | 방 생성 | 모두 |
| `/app/quiz/join/{roomCode}` | 방 참여 | 모두 |
| `/app/quiz/start/{roomCode}` | 퀴즈 시작 | 호스트만 |
| `/app/quiz/answer/{roomCode}` | 답변 제출 | 참가자 |
| `/app/quiz/next/{roomCode}` | 다음 문제로 이동 (수동) | 호스트만 |
| `/app/quiz/rankings/{roomCode}` | 랭킹 조회 | 모두 |
| `/app/quiz/leave/{roomCode}` | 방 나가기 | 모두 |
| `/app/quiz/cancel/{roomCode}` | 방 취소 | 호스트만 |

### 서버 → 클라이언트 (수신)

| 토픽 | 설명 | 수신 대상 |
|-----|------|----------|
| `/topic/quiz/rooms` | 방 생성 알림 | 전체 |
| `/topic/quiz/{roomCode}/participants` | 참가자 변경 알림 | 방 참가자 |
| `/topic/quiz/{roomCode}/game` | 게임 진행 (문제, 종료 등) | 방 참가자 |
| `/topic/quiz/{roomCode}/rankings` | 랭킹 업데이트 | 방 참가자 |
| `/queue/quiz/result` | 개인 답변 결과 | 개인 |
| `/queue/errors` | 에러 메시지 | 개인 |

## 게임 플로우

```
1. [호스트] 방 생성 요청 (topic, documentId 포함)
   ↓
2. [시스템] FastAPI에 문제 생성 요청 (RAG 기반)
   ↓
3. [시스템] 생성된 문제를 Redis에 저장 + 방 코드 반환
   ↓
4. [참가자들] 방 코드로 입장
   ↓
5. [호스트] 퀴즈 시작
   ↓
6. [시스템] Redis에서 문제 조회 + 첫 번째 문제 브로드캐스트 + 타이머 시작
   ↓
7. [참가자들] 답변 제출
   ↓
8. [시스템/호스트] 다음 문제로 이동
   - 자동: 제한 시간 종료 시
   - 수동: 호스트가 넘기기 버튼 클릭
   ↓
9. 6~8 반복 (모든 문제 완료까지)
   ↓
10. [시스템] 최종 랭킹 발표 및 퀴즈 종료 + Redis 데이터 정리
```

## 점수 계산

```java
기본 점수: 100점
시간 보너스: 최대 50점

시간 보너스 계산 방식:
- 정답일 경우에만 보너스 부여
- 빠르게 답할수록 높은 보너스
- 보너스 = 50 * (1 - 소요시간 / 제한시간)

예시:
- 제한시간 30초, 5초에 정답: 100 + 50 * (1 - 5/30) = 141점
- 제한시간 30초, 25초에 정답: 100 + 50 * (1 - 25/30) = 108점
- 오답: 0점
```

## 메시지 예시

### 방 생성 요청
```json
{
  "hostId": "uuid",
  "title": "과학 퀴즈",
  "topic": "일반 상식과 과학",
  "maxParticipants": 30,
  "questionCount": 10,
  "timePerQuestion": 30,
  "classRoomId": "uuid (optional)",
  "documentId": "uuid (optional, RAG 기반 문제 생성용)"
}
```

### 방 참여 요청
```json
{
  "userId": "uuid"
}
```

### 답변 제출 요청
```json
{
  "userId": "uuid",
  "questionNumber": 1,
  "answerIndex": 2,
  "submittedAt": 1234567890,
  "timeSpent": 5000
}
```

### 문제 브로드캐스트
```json
{
  "questionNumber": 1,
  "questionText": "대한민국의 수도는?",
  "options": ["부산", "인천", "서울", "대구"],
  "timeLimit": 30,
  "difficulty": "Easy",
  "status": "success"
}
```

### 답변 결과 (개인)
```json
{
  "questionNumber": 1,
  "isCorrect": true,
  "points": 141,
  "status": "success"
}
```

### 랭킹 브로드캐스트
```json
{
  "rankings": [
    {
      "rank": 1,
      "userId": "uuid",
      "username": "홍길동",
      "totalScore": 850,
      "correctAnswers": 8,
      "totalQuestions": 10,
      "accuracy": 80.0
    }
  ],
  "totalParticipants": 15,
  "status": "success"
}
```

## REST API

방 정보 조회용 REST API도 제공됩니다.

| 엔드포인트 | 메서드 | 설명 |
|-----------|--------|------|
| `/api/quiz/rooms/active` | GET | 활성 방 목록 |
| `/api/quiz/rooms/{roomCode}` | GET | 방 상세 정보 |
| `/api/quiz/rooms/host/{hostId}` | GET | 호스트의 방 목록 |
| `/api/quiz/rooms/classroom/{classRoomId}` | GET | 수업별 방 목록 |
| `/api/quiz/rooms/{roomCode}/joinable` | GET | 방 입장 가능 여부 |

## FastAPI 연동

### 필수 엔드포인트
FastAPI 서버에 다음 엔드포인트를 구현해야 합니다:

```python
POST /api/v1/quiz/generate
```

### 요청 형식
```json
{
  "topic": "일반 상식과 과학",
  "questionCount": 10,
  "difficulty": "Medium",
  "language": "ko",
  "documentId": "550e8400-e29b-41d4-a716-446655440000 (optional, RAG 기반 문제 생성)"
}
```

### 응답 형식
```json
{
  "data": {
    "questions": [
      {
        "questionNumber": 1,
        "questionText": "문제 내용",
        "options": ["선택지1", "선택지2", "선택지3", "선택지4"],
        "correctAnswer": 2,
        "timeLimit": 30,
        "explanation": "정답 설명",
        "difficulty": "Medium"
      }
    ],
    "topic": "일반 상식과 과학",
    "totalQuestions": 10,
    "status": "success"
  },
  "message": "Quiz generated successfully"
}
```

## 데이터베이스 설정

### PostgreSQL 테이블
```sql
CREATE TABLE quiz_room (
    quiz_room_id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    room_code VARCHAR(20) UNIQUE NOT NULL,
    host_id UUID NOT NULL REFERENCES user_entity(user_id),
    class_room_id UUID REFERENCES class_room(class_room_id),
    status VARCHAR(20) NOT NULL,
    max_participants INTEGER NOT NULL,
    question_count INTEGER NOT NULL,
    time_per_question INTEGER NOT NULL,
    topic VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP
);
```

### Redis 데이터 구조
```
quiz:room:{roomCode}:participants - Hash (참가자 정보)
quiz:room:{roomCode}:questions - List (문제 목록)
quiz:room:{roomCode}:current - String (현재 문제 번호)
quiz:room:{roomCode}:answers:{questionNumber} - Hash (답변 정보)
```

## 타이머 동작 방식

### 자동 전환
1. 문제가 시작되면 `QuizTimerService`가 타이머 스케줄링
2. 제한 시간이 끝나면 자동으로 `moveToNextQuestion()` 호출
3. 다음 문제 브로드캐스트 및 새 타이머 시작
4. 마지막 문제 후 자동으로 퀴즈 종료

### 수동 전환
1. 호스트가 `/app/quiz/next/{roomCode}` 호출
2. 현재 문제의 타이머 취소
3. 다음 문제로 이동
4. 새 문제의 타이머 시작

### 타이머 정리
다음 상황에서 타이머가 자동 취소됩니다:
- 호스트가 수동으로 다음 문제로 넘길 때
- 퀴즈가 종료될 때
- 방이 취소될 때

## 프론트엔드 연동 예시 (JavaScript)

```javascript
// SockJS + STOMP 클라이언트 설정
const socket = new SockJS('http://localhost:8080/ws-quiz');
const stompClient = Stomp.over(socket);

// 연결
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // 방 참여자 업데이트 구독
    stompClient.subscribe('/topic/quiz/' + roomCode + '/participants', function(message) {
        const data = JSON.parse(message.body);
        updateParticipantList(data.allParticipants);
    });

    // 게임 진행 구독
    stompClient.subscribe('/topic/quiz/' + roomCode + '/game', function(message) {
        const data = JSON.parse(message.body);
        if (data.status === 'success') {
            showQuestion(data);
            startTimer(data.timeLimit);
        } else if (data.status === 'finished') {
            showFinalRankings(data.finalRankings);
        }
    });

    // 개인 답변 결과 구독
    stompClient.subscribe('/queue/quiz/result', function(message) {
        const data = JSON.parse(message.body);
        showAnswerResult(data.isCorrect, data.points);
    });
});

// 방 참여
function joinRoom(userId) {
    stompClient.send('/app/quiz/join/' + roomCode, {},
        JSON.stringify({ userId: userId })
    );
}

// 답변 제출
function submitAnswer(userId, questionNumber, answerIndex) {
    stompClient.send('/app/quiz/answer/' + roomCode, {},
        JSON.stringify({
            userId: userId,
            questionNumber: questionNumber,
            answerIndex: answerIndex,
            submittedAt: Date.now(),
            timeSpent: elapsedTime
        })
    );
}

// 다음 문제로 (호스트만)
function nextQuestion(hostId) {
    stompClient.send('/app/quiz/next/' + roomCode, {},
        JSON.stringify({ hostId: hostId })
    );
}
```

## 환경 변수 설정

`application.yaml`에 FastAPI URL 설정:
```yaml
fastapi:
  url: http://localhost:8000
```

## 주의사항

1. **문제 생성 실패**: FastAPI 서버가 응답하지 않으면 **방 생성 실패** (기존: 퀴즈 시작 불가)
2. **동시성**: Redis를 통한 실시간 상태 관리로 동시 접속 처리
3. **타이머 정확도**: 네트워크 지연으로 인해 클라이언트와 서버 타이머가 약간 다를 수 있음
4. **보안**: 현재 WebSocket 엔드포인트는 인증 없이 접근 가능 (추후 개선 필요)
5. **데이터 정리**: 퀴즈 종료 시 Redis 데이터 자동 정리됨 (임시 저장)

## 향후 개선 사항

- [ ] WebSocket 연결 시 JWT 인증 추가
- [ ] 문제별 난이도 조절
- [ ] 힌트 시스템
- [ ] 멀티플레이어 팀전
- [ ] 문제 풀이 히스토리 저장
- [ ] 통계 및 분석 기능

---

## 변경 이력

### 2025-11-22: 방 생성 시 문제 미리 생성

#### 변경 내용
기존에는 퀴즈 시작 시점에 FastAPI로 문제를 생성했으나, **방 생성 시점에 문제를 미리 생성**하도록 변경했습니다.

#### 변경 이유
- 퀴즈 시작 시 대기 시간 제거
- 방 생성 실패 시 빠른 피드백 제공
- documentId를 통한 RAG 기반 문제 생성 지원

#### 변경된 플로우
```
Before: 방 생성 → 퀴즈 시작 → FastAPI 호출 → 문제 생성
After:  방 생성 → FastAPI 호출 → Redis 저장 → 퀴즈 시작 → Redis 조회
```

#### 수정된 파일
| 파일 | 변경 내용 |
|------|----------|
| `QuizBattleService.java` | `createRoom()`에서 FastAPI 호출 후 Redis 저장, `startQuiz()`는 Redis에서 조회 |
| `QuizGenerationRequest.java` | `documentId` 필드 추가 |
| `CreateRoomRequest.java` | `documentId` 필드 추가 |
| `QuizBattleWebSocketController.java` | `createRoom()` 호출 시 `documentId` 전달 |

#### API 변경사항
방 생성 요청에 `documentId` 파라미터 추가 (Optional):
```json
{
  "title": "과학 퀴즈",
  "topic": "일반 상식과 과학",
  "documentId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 2025-11-22: UUID 타입 적용

#### 변경 내용
모든 기본키 및 참조 ID 값을 UUID 타입으로 통일했습니다.

#### 적용된 UUID 필드

| 클래스 | 필드 | 타입 | 설명 |
|--------|------|------|------|
| `QuizRoom` | `quizRoomId` | `UUID` | 퀴즈 방 기본키 |
| `QuizRoom` | `host.userId` | `UUID` | 호스트 사용자 참조 (FK) |
| `QuizRoom` | `classRoom.classRoomId` | `UUID` | 수업 참조 (FK, Optional) |
| `QuizParticipant` | `userId` | `UUID` | 참가자 사용자 ID |
| `QuizRanking` | `userId` | `UUID` | 랭킹 내 사용자 ID |
| `QuizAnswer` | `userId` | `UUID` | 답변 제출 사용자 ID |

#### 적용된 DTO UUID 필드

| DTO | 필드 | 타입 | 설명 |
|-----|------|------|------|
| `CreateRoomRequest` | `classRoomId` | `UUID` | 수업 ID (Optional) |
| `CreateRoomRequest` | `documentId` | `UUID` | RAG 기반 문제 생성용 문서 ID (Optional) |
| `QuizGenerationRequest` | `documentId` | `UUID` | FastAPI 요청용 문서 ID (Optional) |
| `RoomCreatedMessage` | `hostId` | `UUID` | 방 생성자 ID |
| `ParticipantLeftMessage` | `userId` | `UUID` | 퇴장한 사용자 ID |
| `QuizRoomDetailResponse` | `hostId` | `UUID` | 호스트 ID |

#### REST API Path Variable UUID

| 엔드포인트 | 파라미터 | 타입 |
|-----------|----------|------|
| `/api/quiz/rooms/host/{hostId}` | `hostId` | `UUID` |
| `/api/quiz/rooms/classroom/{classRoomId}` | `classRoomId` | `UUID` |

#### Repository Query UUID 파라미터

```java
// QuizRoomJpaRepository
List<QuizRoom> findByHostId(@Param("hostId") UUID hostId);
List<QuizRoom> findByClassRoomIdAndStatusIn(
    @Param("classRoomId") UUID classRoomId,
    @Param("statuses") List<QuizRoomStatus> statuses
);
```

#### JSON 요청/응답 예시 (UUID 형식)

```json
// 방 생성 요청
{
  "title": "과학 퀴즈",
  "topic": "일반 상식과 과학",
  "maxParticipants": 30,
  "questionCount": 10,
  "timePerQuestion": 30,
  "classRoomId": "550e8400-e29b-41d4-a716-446655440000",
  "documentId": "550e8400-e29b-41d4-a716-446655440002"
}

// 방 생성 응답
{
  "roomCode": "ABC123",
  "title": "과학 퀴즈",
  "hostId": "550e8400-e29b-41d4-a716-446655440001",
  "maxParticipants": 30,
  "questionCount": 10,
  "timePerQuestion": 30,
  "status": "success",
  "message": "Room created successfully"
}

// 랭킹 응답
{
  "rankings": [
    {
      "rank": 1,
      "userId": "550e8400-e29b-41d4-a716-446655440001",
      "username": "홍길동",
      "totalScore": 850,
      "correctAnswers": 8,
      "totalQuestions": 10,
      "accuracy": 80.0
    }
  ],
  "totalParticipants": 15,
  "status": "success"
}
```

#### 데이터베이스 스키마 (UUID 적용)

```sql
CREATE TABLE quiz_room (
    quiz_room_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(100) NOT NULL,
    room_code VARCHAR(20) UNIQUE NOT NULL,
    host_id UUID NOT NULL REFERENCES user_entity(user_id),
    class_room_id UUID REFERENCES class_room(class_room_id),
    status VARCHAR(20) NOT NULL,
    max_participants INTEGER NOT NULL,
    question_count INTEGER NOT NULL,
    time_per_question INTEGER NOT NULL,
    topic VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP
);

-- 인덱스
CREATE INDEX idx_quiz_room_host_id ON quiz_room(host_id);
CREATE INDEX idx_quiz_room_class_room_id ON quiz_room(class_room_id);
CREATE INDEX idx_quiz_room_status ON quiz_room(status);
```

#### Redis 데이터 구조 (UUID 키)

```
quiz:room:{roomCode}:participants
  └─ Hash: { "550e8400-e29b-41d4-a716-446655440001": QuizParticipant }

quiz:room:{roomCode}:answers:{questionNumber}
  └─ Hash: { "550e8400-e29b-41d4-a716-446655440001": QuizAnswer }
```

#### 장점
- **일관성**: 모든 엔티티에서 동일한 ID 타입 사용
- **보안**: 예측 불가능한 ID로 열거 공격 방지
- **확장성**: 분산 시스템에서 충돌 없는 ID 생성
- **기존 시스템 호환**: 기존 User, ClassRoom 엔티티와 타입 일치
