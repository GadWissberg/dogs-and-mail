package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.gadarts.dogs.core.systems.CameraSystem.Constants.FAR
import com.gadarts.dogs.core.systems.CameraSystem.Constants.FOV
import com.gadarts.dogs.core.systems.CameraSystem.Constants.NEAR


class CameraSystem : EntitySystem() {
    lateinit var camera: PerspectiveCamera
        private set

    object Constants {
        const val FOV = 67F
        const val NEAR = 0.01F
        const val FAR = 300F
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        val cam = PerspectiveCamera(FOV, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = NEAR
        cam.far = FAR
        cam.update()
        camera = cam
    }
}
