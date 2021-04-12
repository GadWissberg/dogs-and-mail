package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3


class CameraSystem : EntitySystem() {
    lateinit var camera: PerspectiveCamera
        private set

    companion object {
        const val FOV = 67F
        const val NEAR = 0.01F
        const val FAR = 300F
        val INITIAL_POSITION = Vector3(0F, 7F, 3F)
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        val cam = PerspectiveCamera(FOV, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = NEAR
        cam.far = FAR
        cam.position.set(INITIAL_POSITION)
        cam.rotate(Vector3.X, -65F)
        cam.update()
        camera = cam
    }
}
