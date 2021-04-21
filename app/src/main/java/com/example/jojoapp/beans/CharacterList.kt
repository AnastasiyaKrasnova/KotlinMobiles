package com.example.jojoapp.beans

import java.io.Serializable

class CharacterList: Serializable {

    public var characters: ArrayList<Character>

    constructor(_characters: ArrayList<Character>){
        characters=_characters
    }
}