package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController

class InputSystem : GameEntitySystem() {
    private lateinit var debugInput: CameraInputController

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        val camera = engine?.getSystem(CameraSystem::class.java)?.camera
        debugInput = CameraInputController(camera)
        debugInput.autoUpdate = true
        Gdx.input.inputProcessor = debugInput
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        debugInput.update()
    }
}
