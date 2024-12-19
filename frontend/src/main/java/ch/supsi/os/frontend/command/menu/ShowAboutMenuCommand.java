package ch.supsi.os.frontend.command.menu;

import ch.supsi.os.frontend.model.PropertiesHandler;
import ch.supsi.os.frontend.view.AboutPopup;

public class ShowAboutMenuCommand implements MenuCommand {

    private final PropertiesHandler propertiesHandler;

    public ShowAboutMenuCommand(PropertiesHandler propertiesHandler) {
        this.propertiesHandler = propertiesHandler;
    }

    @Override
    public void execute() {
        new AboutPopup(propertiesHandler).show();
    }
}
