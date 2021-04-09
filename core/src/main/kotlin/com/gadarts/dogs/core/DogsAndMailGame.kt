package com.gadarts.dogs.core

import com.badlogic.gdx.Game


class DogsAndMailGame : Game() {

    override fun create() {
        setScreen(GamePlayScreen())
    }

}