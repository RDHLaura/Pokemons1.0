open abstract class Pokemon (var especies: Especies, nivel:Int){
    var nombre:String=especies.name
    var potenciaAtaque:Float=especies.ataque
    var vida:Float=especies.HP
    var experiencia=0
    var full_vida:Float=vida
    var defensa:Float=especies.defensa
    var level:Int=nivel
    var tipo:Tipos=especies.tipo
    var evolucion=false
    var atributoEspecial:Int=especies.tipo.atributoTipo //Incremento de atributos por tipo
    open var estado:Boolean=false //Indica si está cargando o tiene activa la habilidad especial
    var contadorHabJug=0 //contador de turnos que controla la disponibilidad de la habilidad especial
    var nombreAtaque=especies.nombreAtaque

    fun incrementarExp(){
        experiencia+=50
        if(experiencia>=100){
            experiencia=0
            subirNivel()
            incrementarLevel()
        }
    }

    fun ataqueBasico():Float{
        return potenciaAtaque
    }

    open abstract fun inicioataqueEspecial():Float
    open abstract fun finAtaqueEspecial():Float

    fun curar(){
        vida+=full_vida/4
        if (vida>=full_vida)
            vida=full_vida
    }

    fun saludo():String{
        return "¡Hola! soy $nombre!"
    }

    fun gracias_por_la_cura():String{
        return "¡Gracias por la cura!"
    }

    fun danyoRecibido(damage:Float){
        vida-=damage
    }
    fun incrementarLevel(){
        level+=1
    }
    fun subirNivel(){
        var incremento=(10..15).random().toFloat()/100
        full_vida+= vida*incremento
        potenciaAtaque+= potenciaAtaque*incremento
        defensa+=defensa*incremento
        atributoEspecial+= atributoEspecial*incremento.toInt()
        evolucionar()
    }

    fun evolucionar(){
        if (level>=5 && evolucion==false){
            evolucion=true
            var incremento=(20..25).random().toFloat()/100
            full_vida+= vida*incremento
            potenciaAtaque+= potenciaAtaque*incremento
            defensa+= defensa*incremento
            nombre=especies.evolucion
            nombreAtaque=especies.nombreAtaqEvol

        }

    }

    override fun toString(): String {
        return "$nombre:\n Ataque:$potenciaAtaque)\n vida: $vida\n defensa: $defensa\n tipo=$tipo."
    }



}