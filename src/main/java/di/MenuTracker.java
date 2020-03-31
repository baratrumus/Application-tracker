package di;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * Класс событий трекера
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */
public class MenuTracker {

    private Input input;
    private ITracker tracker;
    private Consumer<String> output;

    private List<UserAction> actions = new ArrayList<>();

    public MenuTracker(Input input, ITracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }

    /**
     * Метод заполняет список.
     * 1. создание объекта внутреннего класса происходит через обращение к объекту внешнего класса
     * di.MenuTracker через this
     * this.new
     * <p>
     * 2. ShowAll() сделан статическим классом и его объект создается через обращение к имени внешнего класса
     * di.MenuTracker. уже нет.
     *
     * 3. di.ExitProgram сделан через внешний класс
     */
    public void fillActions(StartUI ui) {
        this.actions.add(this.new AddItem(0, "Добавление новой заявки"));
        this.actions.add(this.new ShowAll(1, "Вывод всех заявок из хранилища"));
        this.actions.add(this.new EditItem(2, "Редактирование заявки"));
        this.actions.add(this.new DeleteItem(3, "Удаление заявки"));
        this.actions.add(this.new FindItemById(4, "Поиск по Id"));
        this.actions.add(this.new FindItemsByName(5, "Поиск заявок по имени"));
        this.actions.add(new ExitProgram(6, "Выход из программы", ui));
    }


    /**
     * Метод позволяет добавлять  новый action в список actions.
     * Это позволяет реализовать принцип класс открыт для расширения через этот метод, но закрыт для изменения,
     * т.к. не надо редактировать внутренний код класса для добавления нового действия в меню.
     * Для расширения есть интерфейс доступа
     *
     * @param action
     */
    public void addAction(UserAction action) {
        this.actions.add(action);

    }

    /**
     * @return геттер длины списка
     */
    public int getActionsLength() {
        return actions.size();
    }

    /**
     * Метод в зависимости от указанного ключа, выполняет соотвествующие действие.
     *
     * @param key ключ операции
     */
    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    /**
     * Метод выводит на экран меню.
     */
    public void show() {
        for (UserAction action : this.actions) {
            if (action != null) {
                output.accept(action.info());
            }
        }
    }

    /**
     * внутренний класс - добавление элемента в трекер(новой заявки в хранилище).
     */
    private class AddItem extends BaseAction {

        public AddItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Добавление новой заявки --------------");
            String name = input.ask("Введите имя заявки :");
            String desc = input.ask("Введите описание заявки :");
            long created = System.currentTimeMillis();
            Item item = new Item(name, desc, created);
            tracker.add(item);
            output.accept(String.format("------------ Добавлена новая заявка с Id : %s-----------", item.getId()));
        }
    }


    /**
     * Метод выводит все заявки из хранилища
     */
    private class ShowAll extends BaseAction {

        public ShowAll(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Вывод всех заявок из хранилища --------------");
            for (Item item : tracker.findAll()) {
                System.out.println("------------ Id : " + item.getId() + " ------------");
                System.out.println("------------ Имя : " + item.getName() + " ------------");
                System.out.println("------------ Описание : " + item.getDecs() + " ------------");
                System.out.println("------------ Дата создания : " + item.getTime() + " ------------");
                System.out.println("");
            }
        }
    }


    /**
     * Метод реализует Редактирование заявки по Id
     */
    private class EditItem extends BaseAction {

        public EditItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Редактирование заявки --------------");
            String id = input.ask("Введите Id заявки для редактирования :");
            Item item = tracker.findById(id);
            if (item != null) {
                output.accept(String.format("------------ Редактируем заявку с именем : %s-----------", item.getName()));
                output.accept(String.format("------------ Описание : %s-----------", item.getDecs()));
                output.accept(String.format("------------ Дата создания(будет заменена на текущую) : %s-----------", item.getTime()));
                String newName = input.ask("Введите новое имя :");
                String newDesc = input.ask("Введите новое описание :");
                long created = System.currentTimeMillis();
                Item newItem = new Item(newName, newDesc, created);
                if (tracker.replace(item.getId(), newItem)) {
                    newItem = tracker.findById(id);
                    output.accept(String.format("------------ Успешно изменена заявка с Id : %s-----------\n"
                                    + "------------ Имя : %s-----------\n"
                                    + "------------ Описание : %s-----------\n"
                                    + "------------ Дата создания : %s-----------\n",
                            newItem.getId(), newItem.getName(), newItem.getDecs(), newItem.getTime()));
                } else {
                    output.accept("------------ Редактирование не удалось -----------");
                }
            } else {
                output.accept("------------ Заявок с таким Id не найдено -----------");
            }
        }

    }


    /**
     * Метод реализует удаление заявки по Id
     */
    private class DeleteItem extends BaseAction {

        public DeleteItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Удаление заявки --------------");
            String id = input.ask("Введите Id заявки для удаления :");
            Item item = tracker.findById(id);
            if (item != null) {
                if (tracker.delete(item.getId())) {
                    output.accept("------------ Заявка успешно удалена ");
                } else {
                    output.accept("------------ Удаление не удалось -----------");
                }
            } else {
                output.accept("------------ Заявок с таким Id не найдено -----------");
            }


        }

    }


    /**
     * Метод реализует поиск заявки по Id
     */
    private class FindItemById extends BaseAction {

        public FindItemById(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Поиск по Id --------------");
            String id = input.ask("Введите Id заявки для поиска :");
            Item item = tracker.findById(id);
            if (item != null) {
                output.accept(String.format("------------ Id : %s-----------\n"
                + "------------ Имя : %s-----------\n"
                + "------------ Описание : %s-----------\n"
                + "------------ Дата создания : %s-----------\n",
                        item.getId(), item.getName(), item.getDecs(), item.getTime()));
            } else {
                output.accept("------------ Заявок с таким Id не найдено -----------");
            }

        }

    }


    /**
     * Метод реализует поиск заявок по имени
     */
    private class FindItemsByName extends BaseAction {

        public FindItemsByName(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, ITracker tracker) {
            output.accept("------------ Поиск заявок по имени --------------");
            String name = input.ask("Введите имя для поиска :");
            List<Item> item = tracker.findByName(name);
            if (item.size() != 0) {
                for (Item it : item) {
                    System.out.println("------------ Id : " + it.getId() + " ------------");
                    System.out.println("------------ Имя : " + it.getName() + " ------------");
                    System.out.println("------------ Описание : " + it.getDecs() + " ------------");
                    System.out.println("------------ Дата создания : " + it.getTime() + " ------------");
                    System.out.println("");
                }
            } else {
                output.accept("------------ Заявок с таким именем не найдено -----------");
            }

        }

    }

}

/**
 * "Внешний" внутренний класс. Сделан через имплементацию, а не наследование,
 * в отличие от других методов меню
 * Выход из программы
 */
class ExitProgram implements UserAction {
    private final StartUI ui;
    private final int key;
    private final String name;

    public ExitProgram(int key, String name, StartUI ui) {
        this.ui = ui;
        this.key = key;
        this.name = name;
    }

    @Override
    public int key() {
        return this.key;
    }

    @Override
    public void execute(Input input, ITracker tracker) {
        this.ui.stop();
    }

    @Override
    public String info() {
        return String.format("%s : %s", this.key, this.name);
    }

}


