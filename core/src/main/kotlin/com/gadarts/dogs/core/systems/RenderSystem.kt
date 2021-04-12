@file:Suppress("DEPRECATION")

package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.AmbientLight
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider
import com.badlogic.gdx.math.Vector3
import com.gadarts.dogs.core.ComponentsMapper
import com.gadarts.dogs.core.ModelInstanceComponent
import com.gadarts.dogs.core.systems.RenderSystem.C.AMBIENT_LIGHT
import com.gadarts.dogs.core.systems.render.AxisModelHandler


class RenderSystem : GameEntitySystem() {
    private lateinit var shadowBatch: ModelBatch
    private lateinit var axisModelHandler: AxisModelHandler
    private lateinit var env: Environment
    private lateinit var shadowLight: DirectionalShadowLight
    private lateinit var camera: PerspectiveCamera
    private lateinit var modelInstanceEntities: ImmutableArray<Entity>
    private lateinit var modelBatch: ModelBatch

    object C {
        val sunLightColor: Color = Color(0.6f, 0.6f, 0.6f, 1f)
        val sunLightDirection: Vector3 = Vector3(-1f, -0.5f, -0.8f)
        const val AMBIENT_LIGHT: Float = 0.4F
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        modelBatch = ModelBatch()
        if (engine != null) {
            val family = Family.all(ModelInstanceComponent::class.java).get()
            modelInstanceEntities = engine.getEntitiesFor(family)
            camera = engine.getSystem(CameraSystem::class.java).camera
        }
        initializeEnv()
        axisModelHandler = AxisModelHandler()
    }


    private fun initializeModelShadowLight() {
        shadowBatch = ModelBatch(DepthShaderProvider())
        shadowLight = DirectionalShadowLight(1024, 1024,
                Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(),
                1F, 300F
        )
        shadowLight.set(0.1F, 0.1F, 0.1F, 0F, -1F, -0.5F)
        env.add(shadowLight)
        env.shadowMap = shadowLight
    }

    private fun initializeEnv() {
        env = Environment()
        env.set(ColorAttribute(AmbientLight, AMBIENT_LIGHT, AMBIENT_LIGHT, AMBIENT_LIGHT, 1f))
        env.add(DirectionalLight().setDirection(C.sunLightDirection).setColor(C.sunLightColor))
        initializeModelShadowLight()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        shadowLight.begin(shadowLight.camera)
        renderModels(shadowBatch, true, shadowLight.camera)
        shadowLight.end()
        resetDisplay(BLACK)
        renderModels(modelBatch)
    }

    private fun renderModels(batch: ModelBatch) {
        renderModels(batch, false, camera)
    }

    private fun renderModels(batch: ModelBatch, forShadows: Boolean, camera: Camera) {
        batch.begin(camera)
        axisModelHandler.render(batch)
        for (entity in modelInstanceEntities) {
            val modelInstanceComponent = ComponentsMapper.Map.modelInstance.get(entity)
            if (!forShadows or modelInstanceComponent.hasShadow) {
                batch.render(modelInstanceComponent.modelInstance, env)
            }
        }
        batch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
        axisModelHandler.dispose()
    }

    private fun resetDisplay(@Suppress("SameParameterValue") color: Color) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        var coveSampling = 0
        if (Gdx.graphics.bufferFormat.coverageSampling) {
            coveSampling = GL20.GL_COVERAGE_BUFFER_BIT_NV
        }
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or coveSampling)
    }
}
