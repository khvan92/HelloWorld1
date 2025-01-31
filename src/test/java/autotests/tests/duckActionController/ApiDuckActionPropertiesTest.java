package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/properties")
public class ApiDuckActionPropertiesTest extends DuckActionsClient {

    @Test(description = "Проверка action properties утки из материала wood и четным ID")
    @CitrusTest
    public void propertiesMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
//        createDuckWithEvenId(runner, "yellow", 0.15, "wood", "quack", WingState.ACTIVE);

        Duck duckWood = new Duck()
                .color("yellow")
                .height(0.15)
                .material("wood")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

        createDuckByDb(runner, "6723882", duckWood);
        validateDuckInDb(runner, "${duckId}", duckWood);
        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "wood");
    }

    @Test(description = "Проверка action properties утки из материала rubber и нечетным ID")
    @CitrusTest
    public void propertiesMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
//        createDuckWithOddId(runner, "yellow", 0.15, "rubber", "quack", WingState.ACTIVE);

        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

        createDuckByDb(runner, "6723883", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);
        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "rubber");
    }
}