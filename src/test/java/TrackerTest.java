
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;

public class TrackerTest {
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        Tracker tracker = new Tracker();
        long created = System.currentTimeMillis();
        Item item = new Item("test1", "testDescription", created);
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void whenReplaceNameThenReturnNewName() {
        Tracker tracker = new Tracker();
        Item previous = new Item("test1", "testDescription", 123L);
        // Добавляем заявку в трекер. Теперь в объект проинициализирован id.
        tracker.add(previous);
        // Создаем новую заявку.
        Item next = new Item("test2", "testDescription2", 1234L);
        // Проставляем старый id из previous, который был сгенерирован выше.
        next.setId(previous.getId());
        // Обновляем заявку в трекере.
        tracker.replace(previous.getId(), next);
        // Проверяем, что заявка с таким id имеет новые имя test2.
        assertThat(tracker.findById(previous.getId()).getName(), is("test2"));
    }

    @Test
    public void whenFindByIdNewItemThenTrackerHasSameName() {
        Tracker tracker = new Tracker();
        Item item = new Item("test1", "testDescription", 123L);
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));

    }

    @Test
    public void whenFindByNameNewItemsThenTrackerArrayHasSameNames() {
        Tracker tracker = new Tracker();
        Item item = new Item("test1", "testDescription", 123L);
        Item item1 = new Item("test1", "testDescription11", 1243L);
        Item item2 = new Item("test2", "testDescription", 123456L);
        tracker.add(item);
        tracker.add(item1);
        tracker.add(item2);
        List<Item> result = tracker.findByName(item.getName());
        for (Item it: result) {
            assertThat(it.getName(), is(item.getName()));
        }
    }

    @Test
    public void whenDeleteIdThenNoSuchId() {
        Tracker tracker = new Tracker();
        Item item = new Item("test1", "testDescription", 123L);
        Item item1 = new Item("test2", "testDescription11", 1243L);
        Item item2 = new Item("test3", "testDescription", 123456L);
        Item item3 = new Item("test4", "testDescription", 23456L);
        tracker.add(item);
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        String saveId = item1.getId();
        tracker.delete(saveId);
        assertNull(tracker.findById(saveId));
    }

    @Test
    public void whenFindAllNewItemsThenTrackerArrayHasSameItems() {
        Tracker tracker = new Tracker();
        Item item = new Item("test1", "testDescription", 123L);
        Item item1 = new Item("test1", "testDescription11", 1243L);
        Item item2 = new Item("test2", "testDescription", 123456L);
        tracker.add(item);
        tracker.add(item1);
        tracker.add(item2);
        List<Item> result = tracker.findAll();
        assertThat(3, is(result.size()));

    }
}