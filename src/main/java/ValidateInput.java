import java.util.List;

/**
 * ValidateInput это Декоратор.  Т.е. мы имеем ссылку на родительский интерфейс Input, чтобы можно было этой ссылке
 * присваивать другую реализацию Input, а именно ConsoleInput или StubInput для теста.
 * Далее к этой реализации мы дополняем функционал ValidateInput
 */

public class ValidateInput implements Input {

    /**
     * Поле, содержащее источник данных
     * мы можем в это поле передать класс ConsoleInput или StubInput
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
               //вызов ask того input, что был передан в конструктор ValidateInput, т.е.
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
