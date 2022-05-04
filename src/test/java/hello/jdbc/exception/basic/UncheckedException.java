package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedException {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(()-> service.callThrow()).isInstanceOf(MyUncheckedException.class);
    }

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }

    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException ex) {
                log.info("exception handling, messages={}", ex.getMessage(), ex);
            }
        }

        public void callThrow() {
            repository.call();
        }
    }
}
