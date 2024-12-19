package ch.supsi.os.frontend.command.pipeline;

import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.controller.ImageController;

public class RotateRightCommand implements PipelineCommand {

    private final ImageController imageController = ImageController.getInstance();
    private final String description;

    public RotateRightCommand() {
        this.description = LocalizationService.getLocalizedString("ui.command.rotateright");
    }

    @Override
    public void execute(Image image) {
        imageController.rotateRight(image);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
