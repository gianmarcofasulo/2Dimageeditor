package ch.supsi.os.frontend.command.pipeline;

import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.controller.ImageController;

public class FlipVerticallyCommand implements PipelineCommand {

    private final ImageController imageController = ImageController.getInstance();
    private final String description;

    public FlipVerticallyCommand() {
        this.description = LocalizationService.getLocalizedString("ui.command.flipvertically");
    }

    @Override
    public void execute(Image image) {
        imageController.flipVertical(image);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
