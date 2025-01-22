package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckActionPropertiesTest extends DuckActionsClient {

    @Test(description = "Проверка action properties утки из материала wood и четным ID")
    @CitrusTest
    public void propertiesMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckWithEvenId(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "wood");
    }

    @Test(description = "Проверка action properties утки из материала rubber и нечетным ID")
    @CitrusTest
    public void propertiesMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckWithOddId(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "rubber");
    }
}