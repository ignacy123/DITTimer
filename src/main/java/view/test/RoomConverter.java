package view.test;

import javafx.util.StringConverter;
import model.conn.Room;

public class RoomConverter extends StringConverter<Room> {

    @Override
    public String toString(Room room) {
        return room.getHost().getName()+"'s room for: "+room.getType();
    }

    @Override
    public Room fromString(String s) {
        return null;
    }
}
