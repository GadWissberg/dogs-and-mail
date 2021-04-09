package com.gadarts.dogs.core

import com.badlogic.ashley.core.ComponentMapper

class ComponentsMapper {
    object Map {
        val modelInstance: ComponentMapper<ModelInstanceComponent> = ComponentMapper.getFor(ModelInstanceComponent::class.java)
    }

}
