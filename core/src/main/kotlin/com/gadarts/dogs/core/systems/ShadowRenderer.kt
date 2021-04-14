@file:Suppress("DEPRECATION")

package com.gadarts.dogs.core.systems

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider

class ShadowRenderer {
    internal lateinit var shadowLight: DirectionalShadowLight
    internal lateinit var shadowBatch: ModelBatch
    internal fun init(env: Environment) {
        shadowBatch = ModelBatch(DepthShaderProvider())
        shadowLight = DirectionalShadowLight(2056, 2056,
                16F, 16F,
                0.1F, 300F
        )
        shadowLight.set(.3F, .3F, .3F, -0.5F, -1F, -0.5F)
        env.add(shadowLight)
        env.shadowMap = shadowLight
    }

    fun begin() {
        shadowLight.begin(shadowLight.camera)
    }

    fun end() {
        shadowLight.end()
    }

}
