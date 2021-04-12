package com.gadarts.dogs.core

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.gadarts.dogs.core.systems.*


class GamePlayScreen : Screen {
    private lateinit var groundModel: Model
    private lateinit var assetsManager: AssetManager
    private lateinit var engine: PooledEngine

    companion object {
        const val FORMAT_MODEL = ".g3dj"
        const val MODEL_SCENE = "scene"
        val auxVector1 = Vector3()
        val auxVector2 = Vector3()
        val auxVector3 = Vector3()
        val auxVector4 = Vector3()
        val auxVector5 = Vector3()
        val groundColor: Color = Color.valueOf("51A30A")
    }

    override fun show() {
        this.engine = PooledEngine()
        assetsManager = AssetManager()
        assetsManager.load(MODEL_SCENE + FORMAT_MODEL, Model::class.java)
        assetsManager.finishLoading()
        addSystems()
        addGround()
        addScene()
    }

    private fun addGround() {
        createGroundModel()
        val ground = engine.createEntity()
        val component = engine.createComponent(ModelInstanceComponent::class.java)
        component.init(ModelInstance(groundModel), false)
        ground.add(component)
        engine.addEntity(ground)
    }

    private fun createGroundModel() {
        val modelBuilder = ModelBuilder()
        modelBuilder.begin()
        val meshPartBuilder: MeshPartBuilder = createGroundMeshPartModelBuilder(modelBuilder)
        val halfSize = 10F

        meshPartBuilder.rect(
                auxVector1.set(halfSize, 0.01F, halfSize),
                auxVector2.set(halfSize, 0.01F, -halfSize),
                auxVector3.set(-halfSize, 0.01F, -halfSize),
                auxVector4.set(-halfSize, 0.01F, halfSize),
                auxVector5.set(0F, 1F, 0F))

        groundModel = modelBuilder.end()
    }

    private fun createGroundMeshPartModelBuilder(modelBuilder: ModelBuilder): MeshPartBuilder {
        val material = Material("ground", ColorAttribute.createDiffuse(groundColor))
        return modelBuilder.part("ground",
                GL20.GL_TRIANGLES,
                (Usage.Position or Usage.Normal or Usage.TextureCoordinates).toLong(),
                material)
    }

    private fun addSystems() {
        engine.addSystem(CameraSystem())
        engine.addSystem(RenderSystem())
        engine.addSystem(InputSystem())
        engine.addSystem(HudSystem())
        engine.addSystem(ProfilingSystem())
    }

    private fun addScene() {
        val scene = engine.createEntity()
        val sceneModelInstanceComponent = engine.createComponent(ModelInstanceComponent::class.java)
        val sceneModel = assetsManager.get(MODEL_SCENE + FORMAT_MODEL, Model::class.java)
        sceneModelInstanceComponent.init(ModelInstance(sceneModel))
        scene.add(sceneModelInstanceComponent)
        engine.addEntity(scene)
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        engine.systems.forEach { system: EntitySystem ->
            val gameEntitySystem: GameEntitySystem = system as GameEntitySystem
            gameEntitySystem.dispose()
        }
        groundModel.dispose()
    }

}
