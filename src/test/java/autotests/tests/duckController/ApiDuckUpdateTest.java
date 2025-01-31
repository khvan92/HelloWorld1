package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckUpdateTest extends DuckActionsClient {

    @Test(description = "Проверка изменения цвета и высоты утки")
    @CitrusTest
    public void colorHeightUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck().color("yellow").height(0.15)
                .material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        Duck updatedByDbDuck = new Duck().color("green").height(0.3)
                .material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        Duck updatedByApiDuck = new Duck().color("red").height(2.0)
                .material("rubber").sound("quack").wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "99997", duckRubber);
        updateDuckByDb(runner, "${duckId}", updatedByDbDuck);
        validateDuckInDb(runner, "${duckId}", updatedByDbDuck);

        duckUpdate(runner, "${duckId}", updatedByApiDuck.color(), updatedByApiDuck.height(),
                updatedByApiDuck.material(), updatedByApiDuck.sound());
        validateResponse(runner, HttpStatus.OK,
                "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
        validateDuckInDb(runner, "${duckId}", updatedByApiDuck);

//        duckProperties(runner, "${duckId}");
//        checkAllProperties(runner, "red", 2, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка изменения цвета и звука утки")
    @CitrusTest
    public void colorSoundUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck().color("yellow").height(0.15)
                .material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        Duck updatedByDbDuck = new Duck().color("white").height(0.15)
                .material("rubber").sound("meow").wingsState(WingState.ACTIVE);
        Duck updatedByApiDuck = new Duck().color("red").height(0.15)
                .material("rubber").sound("piupiu").wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "5687654", duckRubber);
        updateDuckByDb(runner, "${duckId}", updatedByDbDuck);
        validateDuckInDb(runner, "${duckId}", updatedByDbDuck);

        duckUpdate(runner, "${duckId}", updatedByApiDuck.color(), updatedByApiDuck.height(),
                updatedByApiDuck.material(), updatedByApiDuck.sound());
        validateResponse(runner, HttpStatus.OK,
                "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
        validateDuckInDb(runner, "${duckId}", updatedByApiDuck);

//        duckProperties(runner, "${duckId}");
//        checkAllProperties(runner, "green", 0.15, "rubber", "woof", "ACTIVE");
    }
}