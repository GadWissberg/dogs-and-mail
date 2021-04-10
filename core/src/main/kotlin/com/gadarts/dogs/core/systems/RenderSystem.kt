package com.gadarts.dogs.core.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal
import com.badlogic.gdx.graphics.VertexAttributes.Usage.Position
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.*
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.gadarts.dogs.core.ComponentsMapper
import com.gadarts.dogs.core.ModelInstanceComponent
import com.gadarts.dogs.core.systems.RenderSystem.C.AMBIENT_LIGHT


class RenderSystem : GameEntitySystem() {
    private lateinit var axisModelInstanceX: ModelInstance
    private lateinit var axisModelInstanceY: ModelInstance
    private lateinit var axisModelInstanceZ: ModelInstance
    private lateinit var axisModelX: Model
    private lateinit var axisModelY: Model
    private lateinit var axisModelZ: Model
    private val auxVector3_1: Vector3 = Vector3()
    private val auxVector3_2: Vector3 = Vector3()
    private lateinit var env: Environment
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
        createAxis()
    }

    private fun createAxis() {
        val modelBuilder = ModelBuilder()
        axisModelX = createAxisModel(modelBuilder, auxVector3_2.set(1F, 0F, 0F), RED)
        axisModelInstanceX = ModelInstance(axisModelX)
        axisModelY = createAxisModel(modelBuilder, auxVector3_2.set(0F, 1F, 0F), GREEN)
        axisModelInstanceY = ModelInstance(axisModelY)
        axisModelZ = createAxisModel(modelBuilder, auxVector3_2.set(0F, 0F, 1F), BLUE)
        axisModelInstanceZ = ModelInstance(axisModelZ)
        transformAxisModel()
    }

    private fun transformAxisModel() {
        scaleAxis()
        axisModelInstanceX.transform.translate(0F, 0.1F, 0F)
        axisModelInstanceY.transform.translate(0F, 0.1F, 0F)
        axisModelInstanceZ.transform.translate(0F, 0.1F, 0F)
    }

    private fun scaleAxis() {
        axisModelInstanceX.transform.scale(2F, 2F, 2F)
        axisModelInstanceX.transform.translate(0F, 0.2F, 0F)
        axisModelInstanceY.transform.scale(2F, 2F, 2F)
        axisModelInstanceY.transform.translate(0F, 0.2F, 0F)
        axisModelInstanceZ.transform.scale(2F, 2F, 2F)
        axisModelInstanceZ.transform.translate(0F, 0.2F, 0F)
    }

    private fun createAxisModel(modelBuilder: ModelBuilder, dir: Vector3, color: Color): Model {
        return modelBuilder.createArrow(
                auxVector3_1.setZero(),
                dir,
                Material(createDiffuse(color)),
                (Position or Normal).toLong())
    }

    private fun initializeEnv() {
        env = Environment()
        env.set(ColorAttribute(AmbientLight, AMBIENT_LIGHT, AMBIENT_LIGHT, AMBIENT_LIGHT, 1f))
        env.add(DirectionalLight().setDirection(C.sunLightDirection).setColor(C.sunLightColor))
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        resetDisplay(BLACK)
        renderModels()
    }

    private fun renderModels() {
        modelBatch.begin(camera)
        modelBatch.render(axisModelInstanceX)
        modelBatch.render(axisModelInstanceY)
        modelBatch.render(axisModelInstanceZ)
        for (entity in modelInstanceEntities) {
            modelBatch.render(ComponentsMapper.Map.modelInstance.get(entity).modelInstance, env)
        }
        modelBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
        axisModelX.dispose()
        axisModelY.dispose()
        axisModelZ.dispose()
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
