
import di.StubInput;
import di.ValidateInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValidateInputTest {
    private final ByteArrayOutputStream mem = new ByteArrayOutputStream();
    private final PrintStream stdOut = System.out;

    @Before
    public void loadMem() {
        System.out.println("before method - Заменяем стандартный вывод на вывод в память для тестирования");
        System.setOut(new PrintStream(this.mem));
    }

    @After
    public void loadSys() {
        System.out.println("after method - возвращаем обратно стандартный вывод в консоль.");
        System.setOut(this.stdOut);
    }

    @Test
    public void whenInvalidInput() {
        //"1" добавляем в массив чтобы успешно выйти из while в di.ValidateInput
        ValidateInput input = new ValidateInput(
                new StubInput(new String[] {"invalid", "1"})
        );
        List<Integer> list = new ArrayList<>();
        list.add(1);
        input.ask("Enter", list);
        assertThat(this.mem.toString(),
                is(String.format("Введите цифру, а не букву%n")));
    }

    @Test
    public void whenInvalidDigitInput() {
        ValidateInput input = new ValidateInput(
                new StubInput(new String[] {"9", "1"})
        );
        List<Integer> list = new ArrayList<>();
        list.add(1);
        input.ask("Enter", list);
        assertThat(this.mem.toString(),
                is(String.format("Выберите цифру из меню%n")));
    }
}
