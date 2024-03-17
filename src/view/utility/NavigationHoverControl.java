package view.utility;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;


public class NavigationHoverControl {

    private final Rectangle sellingLine;

    private  final Rectangle ticketsLine;

    private final Rectangle eventsLine;

    private final MFXButton eventsNavButton;

    private final MFXButton sellingNavButton;

    private final MFXButton ticketingNavButton;

    private final static Color INITIAL_COLOR = Color.web("#DAC0A3");
    private final static Color HOVER_COLOR = Color.web("#102C57");
    private final SimpleObjectProperty<Color> eventColor = new SimpleObjectProperty<Color>(INITIAL_COLOR);
    private final SimpleObjectProperty<Color> sellingColor = new SimpleObjectProperty<Color>(INITIAL_COLOR);
    private final SimpleObjectProperty<Color> ticketingColor = new SimpleObjectProperty<Color>(INITIAL_COLOR);
    private final Map<MFXButton, PauseTransition> buttonPauseTransitions = new HashMap<>();

    public NavigationHoverControl(Rectangle eL, Rectangle sL, Rectangle tL, MFXButton eNButton, MFXButton sNButton, MFXButton tNButton) {
        this.eventsLine = eL;
        this.sellingLine = sL;
        this.ticketsLine = tL;
        this.eventsNavButton = eNButton;
        this.sellingNavButton = sNButton;
        this.ticketingNavButton = tNButton;

    }

    public void initializeNavButtons() {
        eventsLine.fillProperty().bind(eventColor);
        sellingLine.fillProperty().bind(sellingColor);
        ticketsLine.fillProperty().bind(ticketingColor);

        addOnHoverListener(eventsNavButton, eventColor);
        addOnExitListener(eventsNavButton, eventColor);

        addOnHoverListener(sellingNavButton, sellingColor);
        addOnExitListener(sellingNavButton, sellingColor);

        addOnHoverListener(ticketingNavButton, ticketingColor);
        addOnExitListener(ticketingNavButton, ticketingColor);
    }

    private void addOnHoverListener(MFXButton button, SimpleObjectProperty<Color> color) {
        PauseTransition pauseTransition = buttonPauseTransitions.computeIfAbsent(button, b -> new PauseTransition(Duration.millis(100)));
        button.setOnMouseEntered(event -> {
            pauseTransition.setOnFinished(e -> color.setValue(HOVER_COLOR));
            pauseTransition.playFromStart();
        });
    }

    private void addOnExitListener(MFXButton button, SimpleObjectProperty<Color> color) {
        PauseTransition pauseTransition = buttonPauseTransitions.computeIfAbsent(button, b -> new PauseTransition(Duration.millis(100)));
        button.setOnMouseExited(event -> {
            pauseTransition.setOnFinished(e -> color.setValue(INITIAL_COLOR));
            pauseTransition.playFromStart();
        });
    }

}
