package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.scenes.scene2d.Stage

class HudSystem : GameEntitySystem() {
    lateinit var stage: Stage
    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        stage = Stage()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        stage.act(deltaTime)
        stage.draw()
    }
    override fun dispose() {
        TODO("Not yet implemented")
    }

}
