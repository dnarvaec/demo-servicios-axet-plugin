package runner;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RunnerAllTest
 * Ejecuta TODA la suite de features de Karate en paralelo.
 *
 * Uso: mvn test -pl "tests/Automatizacion api/karate"
 * Con entorno: mvn test -Dkarate.env=staging
 */
class RunnerAllTest {

    @Test
    void runAll() {
        Results results = Runner.path("classpath:karate")
                                .tags("~@ignore")
                                .parallel(4);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
