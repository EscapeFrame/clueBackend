# CLUE Backend

CLUE 서비스의 백엔드 서버입니다. Spring Boot 기반의 REST API 서버로, 실시간 퀴즈 배틀, AI 에이전트, 수업 자료 관리 등 핵심 기능을 제공합니다.

## 기술 스택

| 분류 | 기술                                        |
|------|-------------------------------------------|
| Language / Framework | Java 17 / Spring Boot 3.4.4               |
| Database | PostgreSQL 15                             |
| Cache | Redis                                     |
| ORM | JPA / Hibernate, QueryDSL                 |
| 인증 | OAuth2 (Google), JWT                      |
| 파일 저장 | AWS S3                                    |
| 실시간 통신 | WebSocket (STOMP + SockJS)                |
| 외부 연동 | OpenFeign (FastAPI AI 서버, LinkSave, NEIS) |
| API 문서 | Springdoc OpenAPI (Swagger)               |
| 컨테이너 | Docker, Docker Compose                    |

## 프로젝트 구조

```
src/main/java/hello/cluebackend/
├── presentation/        # REST 컨트롤러, WebSocket 핸들러
├── application/         # DTO, Mapper, Facade
├── domain/              # 엔티티, 비즈니스 로직 서비스
├── infrastructure/      # JPA 레포지토리, Feign 클라이언트, 보안
├── config/              # 설정 클래스
└── common/              # 공통 유틸, 예외 처리
```

## 로컬 개발 환경 설정

### 사전 요구사항

- Java 17
- Docker & Docker Compose

### 환경변수 설정

프로젝트 루트에 `.env` 파일을 생성하고 아래 값을 채웁니다.

```env
# Database
POSTGRES_URI=jdbc:postgresql://localhost:5432/clue
POSTGRES_USER=clue1234
POSTGRES_PASSWORD=clue52025

# Redis
# docker-compose 기본값 사용 시 별도 설정 불필요 (localhost:6379)

# JWT
JWT_SECRET=your_jwt_secret_key

# OAuth2 (Google)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
GOOGLE_AUTHORIZATION_URI=https://accounts.google.com/o/oauth2/v2/auth

# AWS S3
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_BUCKET_NAME=your_s3_bucket_name
UPLOAD_FILE_DIR=./uploads

# Frontend / App URL
FRONT_BASE_URL=http://localhost:3000
APP_BASE_URL=http://localhost:8080
APP_LOGIN_REDIRECT_URI=http://localhost:3000/login
APP_REGISTER_REDIRECT_URI=http://localhost:3000/register

# External Services
AI_SERVER_URL=http://your-fastapi-server
LINKSAVE_URL=http://your-linksave-server
NEIS_API_KEY=your_neis_api_key
```

## API 문서

서버 실행 후 아래 주소에서 Swagger UI를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui/index.html
```

## 외부 의존 서비스

| 서비스 | 역할 |
|--------|------|
| FastAPI AI 서버 | RAG 기반 퀴즈 문제 생성, AI 에이전트 기능 |
| LinkSave | 외부 링크 큐레이션 서비스 |
| NEIS Open API | 학교 정보 및 시간표 데이터 조회 |
| Google OAuth2 | 소셜 로그인 |

## 스프링 프로파일

| 프로파일 | 용도 | DDL 전략 |
|----------|------|----------|
| `dev` | 로컬 개발 | `update` |
| `prod` | 프로덕션 배포 | `none` |
| `create` | 초기 스키마 생성 | `create` |
| `createDrop` | 테스트용 (실행 후 삭제) | `create-drop` |
