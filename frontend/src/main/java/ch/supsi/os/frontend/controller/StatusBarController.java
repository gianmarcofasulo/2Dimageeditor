package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.frontend.view.InfoBarView;

import java.util.ResourceBundle;

public class StatusBarController {

    private final EventDispatcher eventDispatcher;
    private final InfoBarView infoBarView;
    private final ResourceBundle resources;

    public StatusBarController(EventDispatcher eventDispatcher, InfoBarView infoBarView, ResourceBundle resources) {
        this.eventDispatcher = eventDispatcher;
        this.infoBarView = infoBarView;
        this.resources = resources;

        String welcomeMessage = resources.getString("ui.welcome.message");
        infoBarView.addWelcomeMessage(welcomeMessage);

        // Subscribe to various events
        eventDispatcher.subscribe(AppEventType.FILE_OPENED, this::handleFileOpened);
        eventDispatcher.subscribe(AppEventType.FILE_SAVED, this::handleFileSaved);
        eventDispatcher.subscribe(AppEventType.FILE_SAVED_AS, this::handleFileSavedAs);
        eventDispatcher.subscribe(AppEventType.LANGUAGE_CHANGED, this::handleLanguageChanged);
        eventDispatcher.subscribe(AppEventType.FILTER_APPLIED, this::handleFilterApplied);
        eventDispatcher.subscribe(AppEventType.TRANSFORMATION_APPLIED, this::handleTransformationApplied);
        eventDispatcher.subscribe(AppEventType.PIPELINE, this::handlePipeline);
        eventDispatcher.subscribe(AppEventType.FILE_OPEN_FAILED, this::handleFileOpenFailed);
        eventDispatcher.subscribe(AppEventType.WELCOME, this::handleWelcomeMessage);

    }

    private void handleWelcomeMessage(Object message) {
        addMessage((String) message);
    }

    private void handleFileOpenFailed(Object fileName) {
        String message = String.format(resources.getString("ui.status.fileOpenFailed"));
        addMessage(message + ": " + fileName);
    }

    private void handlePipeline(Object pipeline) {
        String message = String.format(resources.getString("ui.status.pipeline"));
        addMessage(message + ": " + pipeline);
    }

    private void handleFileOpened(Object fileName) {
        String message = String.format(resources.getString("ui.status.fileOperation"));
        addMessage(message + ": " + fileName);
    }

    private void handleFileSaved(Object fileName) {
        String message = String.format(resources.getString("ui.status.fileOperation"));
        addMessage(message + ": " + fileName);
    }

    private void handleFileSavedAs(Object fileName) {
        String message = String.format(resources.getString("ui.status.fileOperation"));
        addMessage(message + ": " + fileName);
    }

    private void handleLanguageChanged(Object newLanguage) {
        String message = String.format(resources.getString("ui.status.languageChanged"));
        addMessage(message + ": " + newLanguage);
    }

    private void handleFilterApplied(Object filterName) {
        String message = String.format(resources.getString("ui.status.filterApplied"));
        addMessage(message + ": " + filterName);
    }

    private void handleTransformationApplied(Object transformationName) {
        String message = String.format(resources.getString("ui.status.transformationApplied"));
        addMessage(message + ": " + transformationName);
    }

    private void addMessage(String message) {
        infoBarView.addMessage(message);
    }
}
