package bssm.bsm.global.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async("threadPoolTaskExecutor")
    public void run(Runnable runnable) {
        runnable.run();
    }
}
