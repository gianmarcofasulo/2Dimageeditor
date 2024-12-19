package ch.supsi.os.frontend.command.pipeline;

import ch.supsi.os.backend.model.Image;

public interface PipelineCommand {
    void execute(Image image);  // Manipulates the image and returns the modified version
    String getDescription();
}
