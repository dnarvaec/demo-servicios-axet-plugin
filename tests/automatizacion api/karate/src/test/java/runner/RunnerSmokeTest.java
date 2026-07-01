package runner;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RunnerSmokeTest
 * Ejecuta únicamente los scenarios etiquetados con @smoke.
 * Ideal para validación rápida en pipelines CI/CD.
 *
 * Uso: mvn test -pl "tests/Automatizacion api/karate" -Dkarate.options="--tags @smoke"
 */
class RunnerSmokeTest {

    @Test
    void runSmoke() {
        Results results = Runner.path("classpath:karate")
                                .tags("@smoke")
                                .parallel(2);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
