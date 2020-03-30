
/**
 * Создаем класс ошибки выход за границы меню
 * Наследуемся от RuntimeException,  значит  виртуальная машина позволяет нам не обрабатывать его(и не писать throws)
 * При наследовании от просто exception это будет handle exception, т.е. обрабатывать надо обязательно
 * и надо указывать throws в методах и интерфейсах
 */
public class MenuOutException extends RuntimeException {
    public MenuOutException(String msg) {
        super(msg);
    }
}
