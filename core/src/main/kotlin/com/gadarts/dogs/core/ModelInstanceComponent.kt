package com.gadarts.dogs.core

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Pool

class ModelInstanceComponent() : Component, Pool.Poolable {
    var modelInstance: ModelInstance? = null
        private set
    var hasShadow: Boolean = false

    override fun reset() {
        TODO("Not yet implemented")
    }

    fun init(modelInstance: ModelInstance) {
        init(modelInstance, true)
    }

    fun init(modelInstance: ModelInstance, hasShadow: Boolean) {
        this.modelInstance = modelInstance
        this.hasShadow = hasShadow
    }

}
