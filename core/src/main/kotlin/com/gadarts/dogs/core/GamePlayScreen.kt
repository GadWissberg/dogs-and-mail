package com.gadarts.dogs.core

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector2
import com.gadarts.dogs.core.systems.*


class GamePlayScreen : Screen {
    private lateinit var graph: Array<Array<GraphNode>>
    private lateinit var assetsManager: GameAssetManager
    private lateinit var engine: PooledEngine

    companion object {
        const val MODEL_SCENE = "scene"
        const val MODEL_PAVEMENT = "pavement"
        const val GRAPH_WIDTH = 8
        const val GRAPH_HEIGHT = 5
        const val TEST_LEVEL =
                "00101111" +
                        "01111101" +
                        "11010111" +
                        "01111100" +
                        "00100000"
        val PAVEMENT_GRID_ORIGIN: Vector2 = Vector2(-2.5F, -1F)
    }

    override fun show() {
        this.engine = PooledEngine()
        assetsManager = GameAssetManager()
        assetsManager.loadModel(MODEL_SCENE)
        assetsManager.loadModel(MODEL_PAVEMENT)
        assetsManager.finishLoading()
        createGraph()
        addSystems()
        addScene()
    }

    private fun createGraph() {
        val pavementModel = assetsManager.getModel(MODEL_PAVEMENT)
        graph = Array(GRAPH_HEIGHT) { row ->
            Array(GRAPH_WIDTH) { col ->
                val value = Character.getNumericValue(TEST_LEVEL[row * GRAPH_WIDTH + col].toInt())
                var pavement: Entity? = null
                if (value > 0) {
                    pavement = addPavementEntity(pavementModel!!, row, col)
                }
                GraphNode(pavement, row, col)
            }
        }
    }

    private fun addPavementEntity(pavementModel: Model, row: Int, col: Int): Entity? {
        EntityBuilder.begin(engine)
                .addPavementComponent()
                .addModelInstanceComponent(pavementModel, col, row, PAVEMENT_GRID_ORIGIN)
                .finish()
        val pavement = engine.createEntity()
        engine.addEntity(pavement)
        return pavement
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
        val sceneModel = assetsManager.getModel(MODEL_SCENE)
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
    }

}