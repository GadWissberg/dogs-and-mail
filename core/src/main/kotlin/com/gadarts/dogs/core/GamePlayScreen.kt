package com.gadarts.dogs.core

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
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
                        "11111101" +
                        "01010111" +
                        "01111100" +
                        "00010000"
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
                Gdx.app.log("", "" + (row * GRAPH_WIDTH + col))
                val value = TEST_LEVEL[row * GRAPH_WIDTH + col].toInt()
                var pavement: Entity? = null
                if (value > 0) {
                    pavement = addPavementEntity(pavementModel, row, col)
                }
                GraphNode(pavement, row, col)
            }
        }
    }

    private fun addPavementEntity(pavementModel: Model?, row: Int, col: Int): Entity? {
        val pavement = engine.createEntity()
        pavement.add(engine.createComponent(PavementComponent::class.java))
        val modelInsComp = engine.createComponent(ModelInstanceComponent::class.java)
        modelInsComp.init(ModelInstance(pavementModel))
        modelInsComp.modelInstance!!.transform.translate(col.toFloat(), 0F, row.toFloat())
        pavement.add(modelInsComp)
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
