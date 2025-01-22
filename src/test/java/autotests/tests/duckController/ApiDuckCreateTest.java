package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class ApiDuckCreateTest extends DuckActionsClient {



    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        checkMaterial(runner, "rubber");
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void createWoodDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        checkMaterial(runner, "wood");
    }
}