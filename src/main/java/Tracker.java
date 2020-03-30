
import java.util.*;


/**
 * Обертка над строкой.
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */

public class Tracker implements ITracker {
    /**
     * Список для хранение заявок.
     */
    private final List<Item> items = new ArrayList<Item>(100);

    private static final Random RND = new Random();

    /**
     * Метод реализующий добавление заявки в хранилище
     * @param item новая заявка
     */

    public Item add(Item item) {
        item.setId(this.generateId());
        this.items.add(item);
        return item;
    }

    /**
     * Так как у заявки нет уникальности полей имени и описания,
     * Для идентификации нам нужен уникальный ключ.
     * @return Уникальный ключ.
     */
    private String generateId() {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(RND.nextInt());
    }


    public Item findById(String id) {
        Item found = null;
        for (Item item : items) {
            if (item.getId().equals(id)) {
                found = item;
            }
        }
        return found;
    }


    public List<Item> findByName(String key) {
        List<Item> found = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(key)) {
                found.add(item);
            }
        }
        return found;
    }

    /**
     * @return возвращает все элементы списка;
     */
    public List<Item> findAll() {
        return items;
    }


    /**
     * должен заменить ячейку в списке this.items  по id.
     * @param id
     * @param item
     * @return boolean результат - удалось ли провести операцию.
     */
    public boolean replace(String id, Item item) {
        boolean res = false;
        int index;
        if (id == null || item == null) {
            return res;
        }
        for (ListIterator<Item> literator = items.listIterator(); literator.hasNext();) {
            index = literator.nextIndex();
            Item it = literator.next();
            if (it.getId().equals(id)) {
                items.set(index, item);
                res = true;
                items.get(index).setId(id); //ставим id, чтобы он не оказался null, если item берем не из массива
            }
        }
        return res;
    }

    public boolean delete(String id) {
        boolean res = false;
        if (id == null) {
            return res;
        }
        ListIterator<Item> literator = items.listIterator();
        Item currentItem;
        while (literator.hasNext()) {
            currentItem = literator.next();
            if (currentItem.getId().equals(id)) {
                literator.remove();
                res = true;
                break;
            }
        }
        return res;
    }


}