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

echo -e "${GREEN}===== CLUE Backend DEV 실행 =====${NC}"

step "Docker 컨테이너 (postgres, redis) 시작" \
    "docker compose up -d postgres redis"

step "Gradle bootRun (dev) 실행" \
    "./gradlew bootRun --args='--spring.profiles.active=dev'"

echo -e "${GREEN}===== DEV 서버 종료 =====${NC}"
