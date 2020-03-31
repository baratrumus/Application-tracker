package di;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Меню - интерфейс  взаимодействия с пользователем
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */

@Component
public class ConsoleInput implements Input {
    private Scanner scanner = new Scanner(System.in);

    public String ask(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    /**
     * Вызываем overloaded ask(String)
     * @param question
     * @param range
     * throws di.MenuOutException указываем что этот метод может выкинуть этот exception
     * @return число ключа, если он входит в range menu, иначе -1
     */
    public int ask(String question, List<Integer> range) throws MenuOutException {
        int key = Integer.valueOf(this.ask(question));
        boolean keyExist = false;
        for (int value : range) {
            if (value == key) {
                keyExist = true;
                break;
            }
        }
        if (!keyExist) {
            throw new MenuOutException("Out of menu range");
        }
        return key;

    }
}
