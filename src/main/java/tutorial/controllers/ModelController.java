package tutorial.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;
import tutorial.SharedProperties;
import tutorial.model.Grid3D;
import tutorial.rs.Model;
import tutorial.scene.RSMeshGroup;
import tutorial.scene.camera.OrbitCamera;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class ModelController implements Initializable {

    @FXML
    private AnchorPane modelPane;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox paintTexture, wireframe;

    private RSMeshGroup meshGroup;
    private Group scene;
    private final SharedProperties sharedProperties = SharedProperties.getInstance();

    @SneakyThrows(IOException.class)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byte[] modelData = Files.readAllBytes(Path.of("./9638.dat")); // fire cape
        Model model = Model.decode(modelData, -1);
        initBindings();
        initScene(model);
    }

    private void initBindings() {
        sharedProperties.getSelectedColor().bind(colorPicker.valueProperty());
        sharedProperties.getPaintTexture().bind(paintTexture.selectedProperty());
        sharedProperties.getWireframe().bind(wireframe.selectedProperty());
    }

    private void initScene(Model model) {
        meshGroup = new RSMeshGroup(model);
        meshGroup.buildMeshes();
        scene = buildScene();
        Group grid = new Grid3D().create(48f, 1.25f);
        scene.getChildren().add(grid);
        SubScene subScene = create3DScene();
        scene.getChildren().add(new AmbientLight(Color.WHITE));
        modelPane.getChildren().addAll(subScene);
    }

    private Group buildScene() {
        Group group = new Group();
        group.getChildren().addAll(meshGroup.getMeshes());
        return group;
    }

    private SubScene create3DScene() {
        SubScene scene3D = new SubScene(scene, modelPane.getPrefWidth(), modelPane.getPrefHeight(), true, SceneAntialiasing.BALANCED);
        scene3D.setFill(Color.rgb(30, 30, 30));
        new OrbitCamera(scene3D, scene);
        return scene3D;
    }
}
