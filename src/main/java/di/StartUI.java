package di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Меню - интерфейс  взаимодействия с пользователем
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */

@Component
public class StartUI {
    private static final Logger LOG = LoggerFactory.getLogger(StartUI.class);

    private final Input input;
    private final ITracker tracker;
    private Boolean exit;

    private final Consumer<String> output;

    @Bean
    public Consumer<String> createConsumer() {
        return System.out::println;
    }

    /**
     * Конструктор инициализирующий поля.
     * @param input ввод данных.
     * @param tracker хранилище заявок.
     * @param output   Вывод программы сделан через Consumer<String>
     */

    public StartUI(Input input, ITracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.exit = false;
        this.output = createConsumer();
    }

    /**
     * Запуск программы.
     * @param args
     */
    public static void main(String[] args) {
//        Context context = new Context();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("di");
       // context.register(di.ConsoleInput.class);
        context.refresh();
        Input consoleInput = context.getBean(ConsoleInput.class);
        ITracker tracker = context.getBean(Tracker.class);
        context.getBean(StartUI.class).init();
       // new StartUI(new ValidateInput(consoleInput), tracker, false, System.out::println).init();
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
