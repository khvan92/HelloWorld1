package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class ApiDuckActionQuackTest extends DuckActionsClient {

    @Test(description = "Проверка action quack утки с корректным звуком и нечетным ID")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckWithOddId(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        duckQuack(runner, "${duckId}", 1, 1);
        checkSound(runner, "quack");
    }

    @Test(description = "Проверка action quack утки с корректным звуком и четным ID")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckWithEvenId(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        duckQuack(runner, "${duckId}", 1, 1);
        checkSound(runner, "quack");
    }

    public void checkSound(TestCaseRunner runner, String sound) {
        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", sound)
        );
    }
}