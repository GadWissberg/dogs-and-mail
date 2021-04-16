package com.gadarts.dogs.core

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.Model

class GameAssetManager : AssetManager() {
    companion object {
        const val FORMAT_MODEL = ".g3dj"
    }

    fun loadModel(model: String) {
        return load(model + FORMAT_MODEL, Model::class.java)
    }

    fun getModel(model: String): Model? {
        return get(model + FORMAT_MODEL)
    }

}
