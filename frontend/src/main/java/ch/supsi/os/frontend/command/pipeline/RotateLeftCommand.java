package ch.supsi.os.frontend.command.pipeline;

import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.controller.ImageController;

public class RotateLeftCommand implements PipelineCommand {

    private final ImageController imageController = ImageController.getInstance();
    private final String description;

    public RotateLeftCommand() {
        this.description = LocalizationService.getLocalizedString("ui.command.rotateleft");
    }

    @Override
    public void execute(Image image) {
        imageController.rotateLeft(image);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
