package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.gadarts.dogs.core.ComponentsMapper
import com.gadarts.dogs.core.ModelInstanceComponent

class RenderSystem : GameEntitySystem() {
    private lateinit var camera: PerspectiveCamera
    private lateinit var modelInstanceEntities: ImmutableArray<Entity>
    private lateinit var modelBatch: ModelBatch

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        modelBatch = ModelBatch()
        if (engine != null) {
            val family = Family.all(ModelInstanceComponent::class.java).get()
            modelInstanceEntities = engine.getEntitiesFor(family)
            camera = engine.getSystem(CameraSystem::class.java).camera
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        resetDisplay(Color.BLUE)
        modelBatch.begin(camera)
        for (entity in modelInstanceEntities) {
            modelBatch.render(ComponentsMapper.Map.modelInstance.get(entity).modelInstance)
        }
        modelBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
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
