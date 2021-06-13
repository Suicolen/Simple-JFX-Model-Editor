package tutorial.scene;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Scale;
import lombok.Getter;
import tutorial.SharedProperties;
import tutorial.model.Vector3i;
import tutorial.rs.Model;
import tutorial.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RSMeshGroup {
    private final Model model;
    private final List<MeshView> meshes = new ArrayList<>();
    public final float MODEL_SCALE = 0.03f;
    private final SharedProperties sharedProperties = SharedProperties.getInstance();

    public RSMeshGroup(Model model) {
        this.model = model;
    }

    private final Image texture = new Image("file:40.png");

    public void buildMeshes() {
        model.computeUVCoordinates();
        for (int face = 0; face < model.triangleCount; face++) {
            TriangleMesh mesh = new TriangleMesh();
            int faceA = model.faceIndicesA[face];
            int faceB = model.faceIndicesB[face];
            int faceC = model.faceIndicesC[face];

            Vector3i v1 = new Vector3i(model.verticesXCoordinate[faceA], model.verticesYCoordinate[faceA], model.verticesZCoordinate[faceA]);
            Vector3i v2 = new Vector3i(model.verticesXCoordinate[faceB], model.verticesYCoordinate[faceB], model.verticesZCoordinate[faceB]);
            Vector3i v3 = new Vector3i(model.verticesXCoordinate[faceC], model.verticesYCoordinate[faceC], model.verticesZCoordinate[faceC]);

            mesh.getPoints()
                    .addAll(v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z(), v3.x(), v3.y(), v3.z());

            mesh.getFaces().addAll(
                    0, 0, 1, 1, 2, 2
            );
            boolean textured = model.triangleInfo[face] >= 2;
            if (textured) {
                mesh.getTexCoords()
                        .addAll(model.textureUCoordinates[face][0], model.textureVCoordinates[face][0]);
                mesh.getTexCoords()
                        .addAll(model.textureUCoordinates[face][1], model.textureVCoordinates[face][1]);
                mesh.getTexCoords()
                        .addAll(model.textureUCoordinates[face][2], model.textureVCoordinates[face][2]);
            } else {
                mesh.getTexCoords().addAll(0f, 0f, 1f, 0f, 0f, 1f);
            }
            MeshView view = new MeshView(mesh);
            view.getTransforms().add(new Scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE));
            if (textured) {
                PhongMaterial mat = new PhongMaterial();
                mat.setDiffuseMap(texture);
                view.setMaterial(mat);
            } else {
                view.setMaterial(new PhongMaterial(ColorUtils.rs2HSLToColor(model.triangleColors[face], model.faceAlpha == null ? 0 : model.faceAlpha[face])));
            }
            view.drawModeProperty().bind(
                    Bindings.when(
                            sharedProperties.getWireframe())
                            .then(DrawMode.LINE)
                            .otherwise(DrawMode.FILL)
            );
            initListeners(view);
            meshes.add(view);
        }
    }

    private void initListeners(MeshView view) {
        view.setOnMouseClicked(event -> {
            paint(view);
        });
        view.setOnMouseEntered(event -> {
            if (event.isAltDown()) {
                paint(view);
            }
        });
    }

    private void paint(MeshView view) {
        boolean paintTexture = sharedProperties.getPaintTexture().get();
        if (paintTexture) {
            PhongMaterial mat = new PhongMaterial();
            mat.setDiffuseMap(texture);
            view.setMaterial(mat);
        } else {
            Color selectedColor = sharedProperties.getSelectedColor().get();
            view.setMaterial(new PhongMaterial(selectedColor));
        }
    }
}
