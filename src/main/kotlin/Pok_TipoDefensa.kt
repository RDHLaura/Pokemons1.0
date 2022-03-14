class Pok_TipoDefensa:Pokemon {
    constructor(especies: Especies, nivel:Int) : super(especies,nivel)
    init{
        repeat(level-1){
            subirNivel()
            evolucionar()
        }
        vida=full_vida
    }

    override fun inicioataqueEspecial():Float{
        defensa=defensa*super.tipo.atributoTipo
        potenciaAtaque=potenciaAtaque/super.tipo.atributoTipo
        super.contadorHabJug=1
        estado =true
        return super.ataqueBasico()
    }
    override fun finAtaqueEspecial():Float{
        defensa=defensa/super.tipo.atributoTipo
        potenciaAtaque=potenciaAtaque*super.tipo.atributoTipo
        estado =false
        return super.ataqueBasico()
    }


}