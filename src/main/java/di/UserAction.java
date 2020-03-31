package di;

public interface UserAction {
    /**
     * Метод возвращает ключ опции.
     * @return ключ
     */
    int key();

    /**
     * Основной метод. Переходит по цепочке наследования до класса реализующего непосредственное событие,
     * например добавление элемента.
     * @param input объект типа di.Input
     * @param tracker объект типа di.Tracker
     */
    void execute(Input input, ITracker tracker);

    /**
     * Метод возвращает информацию о данном пункте меню.
     * @return Строка меню
     */
    String info();
}
