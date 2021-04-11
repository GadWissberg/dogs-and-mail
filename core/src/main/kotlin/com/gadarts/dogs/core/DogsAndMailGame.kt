package com.gadarts.dogs.core

import com.badlogic.gdx.Game


@Suppress("unused")
class DogsAndMailGame : Game() {

    override fun create() {
        setScreen(GamePlayScreen())
    }

}