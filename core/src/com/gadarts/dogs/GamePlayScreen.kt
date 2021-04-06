package com.gadarts.dogs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Screen
import com.gadarts.dogs.systems.CameraSystem
import com.gadarts.dogs.systems.RenderSystem

class GamePlayScreen : Screen {
    private lateinit var engine: PooledEngine

    override fun show() {
        this.engine = PooledEngine()
        engine.addSystem(RenderSystem())
        engine.addSystem(CameraSystem())
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

}
