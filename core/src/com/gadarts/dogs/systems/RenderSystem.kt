package com.gadarts.dogs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch

class RenderSystem : EntitySystem() {
    private lateinit var modelBatch: ModelBatch

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        modelBatch = ModelBatch()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        resetDisplay(Color.BLUE)
    }

    private fun resetDisplay(@Suppress("SameParameterValue") color: Color) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        var coveSampling = 0
        if (Gdx.graphics.bufferFormat.coverageSampling) {
            coveSampling = GL20.GL_COVERAGE_BUFFER_BIT_NV
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or coveSampling)
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1f)
    }
}
