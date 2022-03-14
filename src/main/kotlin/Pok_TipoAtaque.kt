class Pok_TipoAtaque:Pokemon {
    constructor(especies: Especies, nivel:Int) : super(especies,nivel)

    init{
        repeat(level-1){
            subirNivel()
            evolucionar()
        }
        vida=full_vida
    }

    override fun inicioataqueEspecial():Float{
     //Pongo a cero el contador porque empieza la carga de la habilidad.
        if(super.contadorHabJug>=1){
            super.contadorHabJug=1
            super.estado=true
            return super.ataqueBasico()
        }
        else{
            super.contadorHabJug++
            return super.ataqueBasico()
        }
    }

    override fun finAtaqueEspecial():Float {
        //lanza la habilidad especial en caso de que se haya iniciado, con un valor max de 3 cargas
        var damage: Float
        if (super.estado) {
            if (contadorHabJug in (1..3)) {
                damage = super.ataqueBasico() + (super.contadorHabJug * super.tipo.atributoTipo)
                super.contadorHabJug = 0
                super.estado=false
                return damage
            } else {
                damage = super.ataqueBasico() + (3 * super.tipo.atributoTipo)
                super.contadorHabJug = 0
                return damage
            }
        }
        return super.ataqueBasico()
    }
}