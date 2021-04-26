package com.gadarts.dogs.core

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector2

class EntityBuilder private constructor(private val engine: PooledEngine) {
    companion object {
        private lateinit var builder: EntityBuilder
        private var currentEntity: Entity? = null

        fun begin(engine: PooledEngine): EntityBuilder {
            currentEntity = engine.createEntity()
            builder = EntityBuilder(engine)
            return builder
        }
    }

    fun addPavementComponent(): EntityBuilder {
        currentEntity!!.add(engine.createComponent(PavementComponent::class.java))
        return builder
    }

    fun addModelInstanceComponent(model: Model,
                                  col: Int,
                                  row: Int,
                                  originOffset: Vector2): EntityBuilder {
        val modelInsComp = engine.createComponent(ModelInstanceComponent::class.java)
        modelInsComp.init(ModelInstance(model))
        val transform = modelInsComp.modelInstance!!.transform
        transform.setTranslation(originOffset.x, 0F, originOffset.y)
        transform.translate(col.toFloat(), 0F, row.toFloat())
        currentEntity!!.add(modelInsComp)
        return builder
    }

    fun finish(): Entity {
        val result = currentEntity
        engine.addEntity(currentEntity)
        currentEntity = null
        return result!!
    }
}
