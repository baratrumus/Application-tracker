
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class StartUITest {

    // Создаем буфер для хранения вывода.
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    // получаем ссылку на стандартный вывод в консоль.
    private final PrintStream stdout = new PrintStream(out);
    // создаем консьюмер и переопределяем его accept
    private final Consumer<String> output = new Consumer<String>() {
            @Override
            public void accept(String s) {
                stdout.println(s);
    }
};

    //Создаем объект трекер
    Tracker tracker = new Tracker();
    //Напрямую добавляем заявку
    Item item = tracker.add(new Item("test name", "desc", 54635735));

    //запоминаем шаблон вывода меню
    StringBuilder menuSTRB =  new StringBuilder()
            .append("0 : Добавление новой заявки")
            .append(System.lineSeparator())
            .append("1 : Вывод всех заявок из хранилища")
            .append(System.lineSeparator())
            .append("2 : Редактирование заявки")
            .append(System.lineSeparator())
            .append("3 : Удаление заявки")
            .append(System.lineSeparator())
            .append("4 : Поиск по Id")
            .append(System.lineSeparator())
            .append("5 : Поиск заявок по имени")
            .append(System.lineSeparator())
            .append("6 : Выход из программы")
            .append(System.lineSeparator());


    @Before
    public void loadOutput() {
        System.out.println("before method - Заменяем стандартный вывод на вывод в память для тестирования");
        System.setOut(new PrintStream(this.out));


    }

    @After
    public void backOutput() {
        System.setOut(this.stdout);
        System.out.println("after method - возвращаем обратно стандартный вывод в консоль.");
    }


    @Test
    public void whenUserAddItemThenTrackerHasNewItemWithSameName() {
        Input input = new StubInput(new String[]{"0", "test name", "desc", "6"});   //создаём StubInput с последовательностью действий
        new StartUI(input, tracker, false, output).init();     //   создаём StartUI и вызываем метод init()
        assertThat(tracker.findAll().get(0).getName(), is("test name")); // проверяем, что нулевой элемент массива в трекере содержит имя, введённое при эмуляции.
    }

    @Test
    public void whenUpdateThenTrackerHasUpdatedValue() {
        //создаём StubInput с последовательностью действий(производим замену заявки)
        Input input = new StubInput(new String[]{"2", item.getId(), "test replace", "заменили заявку", "6"});
        // создаём StartUI и вызываем метод init()
        new StartUI(input, tracker, false, output).init();
        // проверяем, что нулевой элемент массива в трекере содержит имя, введённое при эмуляции.
        assertThat(tracker.findById(item.getId()).getName(), is("test replace"));
    }

    @Test
    public void whenDeleteThenTrackerHasDeletedValue() {
        Input input = new StubInput(new String[]{"3", item.getId(), "6"});
        new StartUI(input, tracker, false, output).init();
        assertNull(tracker.findById(item.getId()));
    }

    @Test
    public void whenFindByIdThenTrackerHasThisId() {
        Input input = new StubInput(new String[]{"4", item.getId(), "6"});
        new StartUI(input, tracker, false, output).init();
        assertThat(tracker.findById(item.getId()).getId(), is(item.getId()));
    }

    @Test
    public void whenShowAllThenTrackerHasThisOutput() {
        Input input = new StubInput(new String[]{"1", "6"});
        new StartUI(input, tracker, false, output).init();
        assertThat(
                new String(out.toByteArray()),
                is(
                        new StringBuilder()
                                .append(menuSTRB)
                                .append("------------ Вывод всех заявок из хранилища --------------")
                                .append(System.lineSeparator())
                                .append("------------ Id : " + item.getId() + " ------------")
                                .append(System.lineSeparator())
                                .append("------------ Имя : test name ------------")
                                .append(System.lineSeparator())
                                .append("------------ Описание : desc ------------")
                                .append(System.lineSeparator())
                                .append("------------ Дата создания : " + item.getTime() + " ------------")
                                .append(System.lineSeparator())
                                .append(System.lineSeparator())
                                .append(menuSTRB)
                                .toString()
                )
        );
    }

    @Test
    public void whenFindByNameThenTrackerHasThisOutput() {
        Input input = new StubInput(new String[]{"5", "test name", "6"});
        new StartUI(input, tracker, false, output).init();
        assertThat(
                new String(out.toByteArray()),
                is(
                        new StringBuilder()
                                .append(menuSTRB)
                                .append("------------ Поиск заявок по имени --------------")
                                .append(System.lineSeparator())
                                .append("------------ Id : " + item.getId() + " ------------")
                                .append(System.lineSeparator())
                                .append("------------ Имя : " + item.getName() + " ------------")
                                .append(System.lineSeparator())
                                .append("------------ Описание : " + item.getDecs() + " ------------")
                                .append(System.lineSeparator())
                                .append("------------ Дата создания : " + item.getTime() + " ------------")
                                .append(System.lineSeparator())
                                .append(System.lineSeparator())
                                .append(menuSTRB)
                                .toString()
                )
        );
    }

}
