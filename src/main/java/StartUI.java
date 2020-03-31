
import di.Context;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Меню - интерфейс  взаимодействия с пользователем
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */

public class StartUI {
    private final Input input;
    private final ITracker tracker;
    private boolean exit;
    private final Consumer<String> output;
    /**
     * Конструктор инициализирующий поля.
     * @param input ввод данных.
     * @param tracker хранилище заявок.
     * @param output   Вывод программы сделан через Consumer<String>
     */
    public StartUI(Input input, ITracker tracker, boolean exit, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.exit = exit;
        this.output = output;
    }

    /**
     * Запуск программы.
     * @param args
     */
    public static void main(String[] args) {
//        Context context = new Context();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ConsoleInput.class);
        context.refresh();
        Input consoleInput = context.getBean(ConsoleInput.class);
        new StartUI(new ValidateInput(consoleInput), new Tracker(), false, System.out::println).init();
    }


    /**
     * Основой цикл программы.
     */
    public void init() {

        MenuTracker menu = new MenuTracker(this.input, this.tracker, this.output);
        menu.fillActions(this);
        List<Integer> menuRange = new ArrayList<>();
        for (int i = 0; i < menu.getActionsLength(); i++) {
            menuRange.add(i);
        }
        do {
            menu.show();
            menu.select(Integer.valueOf(input.ask("Введите пункт меню : ", menuRange)));
        } while (!this.exit);
    }

    public void stop() {
        this.exit = true;
    }

}
