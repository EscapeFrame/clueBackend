#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m'

log_info()  { echo -e "${YELLOW}[INFO]${NC} $1"; }
log_ok()    { echo -e "${GREEN}[OK]${NC} $1"; }
log_fail()  { echo -e "${RED}[FAIL]${NC} $1"; }

step() {
    log_info "$1..."
    eval "$2"
    if [ $? -eq 0 ]; then
        log_ok "$1 성공"
    else
        log_fail "$1 실패"
        exit 1
    fi
}

cleanup() {
    log_info "기존 clue-backend 컨테이너 정리..."
    docker rm -f clue-backend 2>/dev/null
}

echo -e "${GREEN}===== CLUE Backend PROD 실행 =====${NC}"

cleanup

step "Gradle 빌드 (테스트 제외)" \
    "./gradlew clean build -x test"

step "Docker 이미지 빌드" \
    "docker build -t clue-backend:latest ."

step "Docker 컨테이너 실행 (prod)" \
    "docker run -d --name clue-backend --env-file .env -p 8080:8080 clue-backend:latest --spring.profiles.active=prod"

echo ""
echo -e "${GREEN}===== PROD 배포 완료 =====${NC}"
echo -e "  컨테이너: clue-backend"
echo -e "  포트: 8080"
echo -e "  로그: docker logs -f clue-backend"
echo ""
echo -e "  중지: ${YELLOW}make prod-stop${NC}"
