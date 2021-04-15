package com.gadarts.dogs.core

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.gadarts.dogs.core.systems.*


class GamePlayScreen : Screen {
    private lateinit var assetsManager: AssetManager
    private lateinit var engine: PooledEngine

    companion object {
        const val FORMAT_MODEL = ".g3dj"
        const val MODEL_SCENE = "scene"
    }

    override fun show() {
        this.engine = PooledEngine()
        assetsManager = AssetManager()
        assetsManager.load(MODEL_SCENE + FORMAT_MODEL, Model::class.java)

        assetsManager.finishLoading()
        addSystems()
        addScene()
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
    }

}
