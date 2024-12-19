package ch.supsi.os.frontend.command.pipeline;

import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.controller.ImageController;

public class NegativeCommand implements PipelineCommand {

    private final ImageController imageController = ImageController.getInstance();    private final String description;

    public NegativeCommand() {
        // Retrieve a static description using LocalizationService
        this.description = LocalizationService.getLocalizedString("ui.command.negative");
    }

    @Override
    public void execute(Image image) {
        imageController.negativeFilter(image);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
