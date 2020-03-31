package di;

import java.util.List;

/**
 * di.ValidateInput это Декоратор.  Т.е. мы имеем ссылку на родительский интерфейс di.Input, чтобы можно было этой ссылке
 * присваивать другую реализацию di.Input, а именно di.ConsoleInput или di.StubInput для теста.
 * Далее к этой реализации мы дополняем функционал di.ValidateInput
 */

public class ValidateInput implements Input {

    /**
     * Поле, содержащее источник данных
     * мы можем в это поле передать класс di.ConsoleInput или di.StubInput
     * и к их поведению добавить поведение валидации.
     *
     */
    private final Input input;

    public ValidateInput(final Input input) {
        this.input = input;
    }

    @Override
    public String ask(String question) {
        return this.input.ask(question);
    }

    @Override
    public int ask(String question, List<Integer> range) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
               //вызов ask того input, что был передан в конструктор di.ValidateInput, т.е.
                // центрального кода, вокруг которого будет наше дополнение - декоратор
                value = this.input.ask(question, range);
                invalid = false;
            } catch (MenuOutException moe) {
                //moe.printStackTrace();                       //Вывод стека exception
                System.out.println("Выберите цифру из меню");
            } catch (NumberFormatException nfe) {            //сюда попадем если введем буквы
                System.out.println("Введите цифру, а не букву");
            }
        } while (invalid);
        return value;
    }
}
