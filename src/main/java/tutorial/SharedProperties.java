package tutorial;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public final class SharedProperties {

    private final ObjectProperty<Color> selectedColor = new SimpleObjectProperty<>(Color.RED);
    private final BooleanProperty paintTexture = new SimpleBooleanProperty();
    private final BooleanProperty wireframe = new SimpleBooleanProperty();

    private static SharedProperties instance;

    public static SharedProperties getInstance() {
        if (instance == null) {
            instance = new SharedProperties();
        }
        return instance;
    }

    private SharedProperties() {

    }
}
