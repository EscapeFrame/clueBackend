package hello.cluebackend.common.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisResetScheduler {
  private final RedisTemplate<String, Object> redisTemplate;

  @Scheduled(cron = "0 59 23 * * *")
  public void resetRedisData() {
    redisTemplate.getConnectionFactory().getConnection().flushDb();
    System.out.println("Redis 데이터 초기화 완료!");
  }
}