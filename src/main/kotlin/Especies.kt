enum class Especies(
    val HP:Float,
    val ataque:Float,
    val defensa:Float,
    val tipo:Tipos,
    val evolucion:String,
    val nombreAtaque:String,
    val nombreAtaqEvol:String,

) {

    BULBASUR(128f,118f,111f,Tipos.PLANTA,"IVYSAUR", "Ciclón de Hojas","Latigazo"),
    CHARMANDER(118f,116f,93f,Tipos.FUEGO, "CHARMALEON", "Llamarada","Incineración"),
    SQUIRTLE(127f,94f,121f, Tipos.AGUA, "WARTORTLE", "Zambullir", "Cúpula de Agua"),
    //MEOWTH(127f,120f,80f, Tipos.ELECTRICO,"PERSIAN","Arañazo", "Zarpazo"),
    PICHU(85f,97f,73f, Tipos.ELECTRICO,"PIKACHU", "Descarga", "Rayo"),
    DIGLETT(100F, 75F, 130F, Tipos.TIERRA, "DUGTRIO", "Cavar", "Muro de piedra")

}