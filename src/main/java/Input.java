
import java.util.List;

/**
 * Интерфейс - контракт для реализации классов ввода данных
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */
public interface Input {

    String ask(String question);

    int ask(String question, List<Integer> range);

}
