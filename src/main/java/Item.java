
import java.util.Objects;

/**
 * Класс представляющий экземпляр заявки.
 * @author ivannikov
 * @version $Id$
 * @since 0.1
 */

public class Item {
    private String id;
    private String name;
    private String decs;
    private long time;

    public Item(String name, String decs, long time) {
        this.name = name;
        this.decs = decs;
        this.time = time;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecs() {
        return this.decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return (time == item.time && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(decs, item.decs));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, decs, time);
    }

}