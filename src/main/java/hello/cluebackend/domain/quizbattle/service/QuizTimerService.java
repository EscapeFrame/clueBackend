package hello.cluebackend.domain.quizbattle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizTimerService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> activeTimers = new ConcurrentHashMap<>();

    public void scheduleQuestionTimeout(String roomCode, int questionNumber, int timeLimit, Runnable onTimeout) {
        String timerKey = getTimerKey(roomCode, questionNumber);
        cancelTimer(timerKey);

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                log.info("Question {} in room {} timed out, moving to next", questionNumber, roomCode);
                onTimeout.run();
                activeTimers.remove(timerKey);
            } catch (Exception e) {
                log.error("Error executing timeout callback for room {} question {}", roomCode, questionNumber, e);
            }
        }, timeLimit, TimeUnit.SECONDS);

        activeTimers.put(timerKey, future);
        log.info("Scheduled timer for room {} question {} with {} seconds", roomCode, questionNumber, timeLimit);
    }

    public void cancelQuestionTimer(String roomCode, int questionNumber) {
        String timerKey = getTimerKey(roomCode, questionNumber);
        cancelTimer(timerKey);
        log.info("Cancelled timer for room {} question {}", roomCode, questionNumber);
    }

    public void cancelAllTimersForRoom(String roomCode) {
        activeTimers.keySet().stream()
                .filter(key -> key.startsWith(roomCode + ":"))
                .forEach(this::cancelTimer);
        log.info("Cancelled all timers for room {}", roomCode);
    }

    public boolean hasActiveTimer(String roomCode, int questionNumber) {
        String timerKey = getTimerKey(roomCode, questionNumber);
        ScheduledFuture<?> future = activeTimers.get(timerKey);
        return future != null && !future.isDone();
    }

    public long getRemainingTime(String roomCode, int questionNumber) {
        String timerKey = getTimerKey(roomCode, questionNumber);
        ScheduledFuture<?> future = activeTimers.get(timerKey);
        if (future != null && !future.isDone()) {
            return future.getDelay(TimeUnit.SECONDS);
        }
        return 0;
    }

    public void scheduleTask(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("Error executing scheduled task", e);
            }
        }, delay, unit);
    }

    public void shutdown() {
        log.info("Shutting down quiz timer service");
        activeTimers.values().forEach(future -> future.cancel(false));
        activeTimers.clear();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void cancelTimer(String timerKey) {
        ScheduledFuture<?> future = activeTimers.remove(timerKey);
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }

    private String getTimerKey(String roomCode, int questionNumber) {
        return roomCode + ":" + questionNumber;
    }
}
