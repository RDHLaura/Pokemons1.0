class gameManager(pokemonJugador:Pokemon, pokemonMaquina: Pokemon) {
    var jugador=pokemonJugador
    var maquina=pokemonMaquina
    var damage=0f

    fun curar(){
        jugador.curar()
    }
    fun subirExp(){
        jugador.incrementarExp()
    }

    fun ganador(pokemon1: Pokemon, pokemon2:Pokemon):Int{
        if(pokemon1.vida<=0)
            return 1
        else if(pokemon2.vida<=0)
            return 2
        else
            return 0
    }

    fun jugadorInicioHabEspecial():Float{
        damage=(jugador.inicioataqueEspecial()/maquina.defensa)*10+1
        maquina.danyoRecibido(damage)
        return damage
    }
    fun jugadorLanzarHbEspecial():Float{
        damage=(jugador.finAtaqueEspecial()/maquina.defensa)*10+1
        maquina.danyoRecibido(damage)
        return damage
    }

    fun jugadorHabBasica():Float{ //ataque básico
        damage=(jugador.ataqueBasico()/maquina.defensa)*10+1
        maquina.danyoRecibido(damage)
        jugador.contadorHabJug++
        return damage
    }
    fun turnoMaquina():Float{
        if(maquina.estado && maquina.contadorHabJug==3){ //Lanza la habilidad especial si está activa
            damage=(maquina.finAtaqueEspecial()/jugador.defensa)*10+1
            jugador.danyoRecibido(damage)
            maquina.contadorHabJug=0
            return damage
        }
        else if (!maquina.estado && maquina.contadorHabJug==2){ //activa la habilidad especial
            damage=(maquina.inicioataqueEspecial()/jugador.defensa)*10+1
            jugador.danyoRecibido(damage)
            maquina.contadorHabJug=0
            return damage

        } else { //ataque básico
            damage=(maquina.ataqueBasico()/jugador.defensa)*10+1
            jugador.danyoRecibido(damage)
            maquina.contadorHabJug++
            return damage
        }
    }
}



