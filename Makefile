.PHONY: dev prod dev-stop prod-stop clean

SCRIPTS_DIR := scripts

dev:
	$(SCRIPTS_DIR)/dev.sh

prod:
	$(SCRIPTS_DIR)/prod.sh

dev-stop:
	@echo "\033[0;33m[INFO]\033[0m DEV 환경 중지..."
	@docker compose down && echo "\033[0;32m[OK]\033[0m DEV 환경 중지 완료" || echo "\033[0;31m[FAIL]\033[0m DEV 환경 중지 실패"

prod-stop:
	@echo "\033[0;33m[INFO]\033[0m PROD 컨테이너 중지..."
	@docker stop clue-backend && docker rm clue-backend && echo "\033[0;32m[OK]\033[0m PROD 컨테이너 중지/삭제 완료" || echo "\033[0;31m[FAIL]\033[0m PROD 컨테이너 중지 실패"

clean:
	@echo "\033[0;33m[INFO]\033[0m 정리 중..."
	@./gradlew clean && docker compose down -v && echo "\033[0;32m[OK]\033[0m 정리 완료" || echo "\033[0;31m[FAIL]\033[0m 정리 실패"
