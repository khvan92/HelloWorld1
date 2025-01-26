package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

public class ApiDuckDeleteTest extends DuckActionsClient {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duckRubber);
        getNewDuckId(runner);
        duckDelete(runner, "${duckId}");
        validateResponseFromResources(runner, HttpStatus.OK, "duckActionsTest/successfulDelete.json");
        duckGetAllIds(runner);
        checkDeletedId(runner);
    }

    public void checkDeletedId(TestCaseRunner runner) {
        AtomicReference<Integer> id = new AtomicReference<>(0);
        AtomicReference<String> allIds = new AtomicReference<String>();
        getIds(runner);
        runner.$(action ->
        {
            id.set(Integer.parseInt(action.getVariable("duckId")));
            allIds.set(action.getVariable("allDucksIds"));

            if(allIds.get().contains(id.get().toString())) {
                action.addException(new CitrusRuntimeException("Уточка не удалена"));
            }
        });
    }
}