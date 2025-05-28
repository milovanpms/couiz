package fr.couiz.ui;

import javafx.application.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private final StringProperty greeting = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContents(), 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Region createContents() {
        VBox results = new VBox(20, createInputRow(), createOutputLabel(), createButton());
        results.setAlignment(Pos.CENTER);
        results.getStylesheets().add(this.getClass().getResource("./style.css").toExternalForm());
        return results;
    }

    private Button createButton() {
        Button results = new Button("Hello");
        results.setOnAction(evt -> setGreeting());
        return results;
    }

    private HBox createInputRow() {
        TextField textField = new TextField("");
        textField.textProperty().bindBidirectional(name);
        Label namePrompt = new Label("Name:");
        namePrompt.getStyleClass().add("prompt-label");
        HBox hBox = new HBox(6, namePrompt, textField);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createOutputLabel() {
        Label results = new Label("");
        results.getStyleClass().add("greeting-label");
        results.textProperty().bind(greeting);
        return results;
    }

    private void setGreeting() {
        greeting.set("Hello " + name.get());
    }
}
