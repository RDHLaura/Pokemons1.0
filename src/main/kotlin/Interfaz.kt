// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlin.random.Random

fun constructorPokemon(especie: Especies, esjugador:Boolean,nivelJugador:Int, dificultad:Int):Pokemon{
    var nivel=nivelJugador
    if (!esjugador){
        nivel=Random.nextInt(nivelJugador+dificultad-1, nivelJugador+dificultad)
        if (nivel<1)
            nivel=1
    }
    var pokemon:Pokemon

    if (especie.tipo.tipoAtaque=="ATAQUE"){
        pokemon=(Pok_TipoAtaque(especie,nivelJugador ))
        return pokemon
    }

    else{
        pokemon=(Pok_TipoDefensa(especie, nivelJugador))
        return pokemon
    }

}

@Composable
@Preview()
fun App() {
    MaterialTheme() {
        var pantallas: Int by remember { mutableStateOf(1) }
        var jugador: Pokemon by remember { mutableStateOf(Pok_TipoAtaque(Especies.CHARMANDER, 5)) }
        var maquina: Pokemon by remember { mutableStateOf(Pok_TipoAtaque(Especies.CHARMANDER, 5)) }
        var juego by remember { mutableStateOf(gameManager(jugador, maquina)) }
        var ganador: Int by remember { mutableStateOf(0) }
        val corrutina = rememberCoroutineScope()
        var enemigosDerrotados: Int by remember { mutableStateOf(0) }
        var dificultad by remember { mutableStateOf(0) }

        @Composable
        fun eleccionPokemon(asignar:(Pokemon)->Unit, asignarMaquina:(Pokemon)->Unit, pantalla:(Int)->Unit){

            var showStats by remember { mutableStateOf(false) }
            Column(modifier = Modifier.fillMaxSize(), ){

            //elección dificultad
                if(dificultad ==0){
                    Box(
                        modifier = Modifier.padding(top=250.dp, start=250.dp)
                    ){
                        Text(text="ELIGE LA DIFICULTAD",color= Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(bottom = 200.dp)) {
                        Button(
                            onClick = {
                                dificultad=-5
                            },
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.size(170.dp, 70.dp)
                        )
                        {
                            Text("Fácil", color = Color(0,128,0), fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        }
                        Button(
                            onClick = {
                                dificultad=1
                            },
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.size(170.dp, 70.dp)
                        )
                        {
                            Text("Normal", color = Color(0,102, 204), fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        }
                        Button(
                            onClick = {
                                dificultad=5
                            },
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.size(170.dp, 70.dp)
                        )
                        {
                            Text("Díficil", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        }
                    }

                }
            //elección pokemon
                else{
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top=200.dp)
                    ){
                        Button(
                            onClick = {
                                showStats=true
                            },
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.size(220.dp, 60.dp).padding(bottom = 20.dp))

                        {
                            Text("Mostar stats básicos", color=Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                    }

                    Row (
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                        , modifier = Modifier.fillMaxWidth().fillMaxHeight()//.padding(top = .dp)
                    ) {
                        for (especie in Especies.values()) {
                            Column {

                                Button(
                                    onClick = {
                                        var nivel=Random.nextInt(1, 10)
                                        if(nivel<1)
                                            nivel=1
                                        var jugador = constructorPokemon(especie, true,nivel,  dificultad)
                                        asignar(jugador)
                                        asignarMaquina(constructorPokemon(Especies.values().random(), false,jugador.level, dificultad))
                                        pantalla(2)
                                    },
                                    colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                                    elevation = ButtonDefaults.elevation(0.dp, 0.dp)
                                )
                                {
                                    Image(
                                        bitmap = useResource("${especie.name}.png") {
                                            loadImageBitmap(it)
                                        }, "${especie.name}",
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(250.dp)
                                    )
                                }

                                if(showStats){
                                    Text("${especie.name}:\n(Stats básicos)\nVida:${especie.HP}\nAtaque: ${especie.ataque}\nDefensa: ${especie.defensa}" +
                                                "\nTipo: ${especie.tipo}",
                                        color = Color.Unspecified, fontWeight = FontWeight.Bold)}
                            }
                        }
                    }
                }
            }
        }


        @Composable
        fun combate(ganador: (Int) -> Unit) {
            juego = gameManager(jugador, maquina)
            var ataquejugador by remember { mutableStateOf(0f) }
            var ataquemaquina by remember { mutableStateOf(0f) }
            var vidamaquina by remember { mutableStateOf(maquina.vida) }
            var vidajugador by remember { mutableStateOf(jugador.vida) }
            var estadojugador: Boolean by remember { mutableStateOf(jugador.estado) }
            var estadomaquina: Boolean by remember { mutableStateOf(maquina.estado) }
            var esturnoMaquina:Boolean by remember { mutableStateOf(false) }
            var mensajeJugador:String by remember { mutableStateOf(jugador.saludo()) }
            var niveljugador:Int by remember { mutableStateOf(jugador.level)}
            var nombreJugador:String by remember { mutableStateOf(jugador.nombre) }
            var yaEvoluciono by remember { mutableStateOf(false) }
            @Composable
            fun turnoMaquina(){
                corrutina.launch(){
                    vidajugador = jugador.vida
                    delay(400L)
                    ataquemaquina = juego.turnoMaquina()
                    estadomaquina=maquina.estado
                    when (juego.ganador(jugador, maquina)){
                        1-> pantallas = 3
                        2->{
                            enemigosDerrotados++
                            juego.subirExp()
                            niveljugador=jugador.level
                            if(jugador.evolucion && !yaEvoluciono){
                                mensajeJugador="¡Evoluciona a ${nombreJugador}!"
                                yaEvoluciono=true
                                corrutina.launch {
                                    delay(2000L)
                                    mensajeJugador=""
                                }
                                nombreJugador=jugador.especies.evolucion
                            }
                            maquina= constructorPokemon(Especies.values().random(), false,jugador.level, dificultad )
                        }
                    }
                    vidamaquina = maquina.vida
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
                , modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(bottom = 100.dp)
              ) {
//columna jugador
                Column(
                    //modifier = Modifier.padding(end=80.dp)
                ){
        //barra carga habilidad especial
                    if(estadojugador){
                        Box(modifier = Modifier.background(Color.DarkGray).size(15.dp, 90.dp)) {
                            Box(
                                modifier = Modifier.background(color = Color.Green)
                                    .size(15.dp, ((jugador.contadorHabJug)*30).dp )
                            ) {}
                        }
                    }
                    Box(modifier = Modifier.size(15.dp))
        //Imagen y barra de vida del jugador
                    Text(text="$nombreJugador lvl $niveljugador", fontWeight= FontWeight.Bold,modifier = Modifier.background(Color.LightGray))
                    Box(modifier = Modifier.background(Color.DarkGray).size(220.dp, 15.dp)) {
                        Box(
                            modifier = Modifier.background(color = Color.Green)
                                .size((vidajugador * 220f/jugador.full_vida).dp, 15.dp)
                        ) {}
                    }

                //Imagen del jugador
                    Image(
                        bitmap = useResource("${nombreJugador}.png") {
                            loadImageBitmap(it)
                        }, "${nombreJugador}",
                        modifier = Modifier.size(200.dp)
                    )
                    Text("Ataque: ${(ataquejugador).toInt()}", color = Color.Unspecified, fontWeight = FontWeight.Bold)
                    Text("Vida: ${jugador.vida.toInt()}", color = Color.Unspecified, fontWeight = FontWeight.Bold)
                    Text(text=mensajeJugador, color = Color.Unspecified, fontWeight = FontWeight.Bold)
                }
//Columna Centro

                Column (horizontalAlignment = Alignment.CenterHorizontally){


        //botón habilidad especial
                    if (!estadojugador) {
                        Button(
                            onClick = {
                                ataquejugador = juego.jugadorInicioHabEspecial()
                                estadojugador = true
                                esturnoMaquina=true
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                            modifier = Modifier.size(220.dp, 50.dp)

                        ) {if(jugador.tipo.tipoAtaque=="ATAQUE")
                            Text("Cargar ${jugador.nombreAtaque}",fontWeight = FontWeight.Bold)
                        else
                            Text("${jugador.nombreAtaque}",fontWeight = FontWeight.Bold)
                        }
                    }
                    else{
                        Button(
                            onClick = {
                                ataquejugador = juego.jugadorLanzarHbEspecial()
                                esturnoMaquina=true
                                estadojugador = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                            modifier = Modifier.size(220.dp, 50.dp)

                        ) {
                            if(jugador.tipo.tipoAtaque=="ATAQUE")
                                Text("Lanzar ${jugador.nombreAtaque}",fontWeight = FontWeight.Bold)
                            else
                                Text("Salir",fontWeight = FontWeight.Bold)
                        }
                    }
        //Botón curar
                    Button(
                        onClick = {
                            juego.curar()
                            esturnoMaquina=true
                            mensajeJugador=jugador.gracias_por_la_cura()
                            corrutina.launch(){
                                delay(5000L)
                                mensajeJugador=""}
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        modifier = Modifier.size(200.dp, 60.dp).padding(10.dp),

                        ) {
                        Text("Curar", color = Color.White)
                    }
                }

//columna de la máquina   //modifier = Modifier.padding(start=80.dp)
                Column() {
            //barra carga habilidad especial
                    if(estadomaquina){
                        Box(modifier = Modifier.background(Color.DarkGray).size(15.dp, 90.dp)) {
                            Box(
                                modifier = Modifier.background(color = Color.Red)
                                    .size(15.dp, ((maquina.contadorHabJug)*30).dp )
                            ) {}
                        }
                    }
                    Box(modifier = Modifier.size(15.dp))
                    //Barra de vida de la máquina
                    Text(text="${maquina.nombre} lvl ${maquina.level}", fontWeight= FontWeight.Bold, modifier = Modifier.background(Color.LightGray))
                    Box(modifier = Modifier.background(Color.DarkGray).size(220.dp, 15.dp)) {
                        Box(
                            modifier = Modifier.background(color = Color.Red)
                                .size((vidamaquina * 220f / maquina.full_vida).dp, 15.dp)
                        ) {}
                    }

            //ataque básico
                    Button(onClick = {
                        ataquejugador = juego.jugadorHabBasica()
                        esturnoMaquina=true
                        },
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent)
                        //elevation = ButtonDefaults.elevation(0.dp, 0.dp)
                        )

                    {
                        Image(
                            bitmap = useResource("${maquina.nombre}.png") {
                                loadImageBitmap(it)
                            }, "${maquina.nombre}",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    if(estadomaquina)
                        Text("${maquina.nombreAtaque}: ${ataquemaquina.toInt()}", color = Color.Red, fontWeight = FontWeight.Bold)
                    else
                        Text("Ataque: ${ataquemaquina.toInt()}", color = Color.Red, fontWeight = FontWeight.Bold)
                    Text("Vida: ${maquina.vida.toInt()}", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
            if(esturnoMaquina){
                turnoMaquina()
                esturnoMaquina=false
            }
        }

        @Composable()
        fun finJuego() {
            ganador = juego.ganador(jugador, maquina)

            Column (
                modifier=Modifier.fillMaxSize().padding(top=200.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

                ){
                   Text(text="¡Fin del Juego!", fontSize = 50.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 50.dp))
                    Text(text="Nivel máximo: ${jugador.level}\nEnemigos derrotados: $enemigosDerrotados", fontSize = 20.sp, color = Color.White,
                                                fontWeight = FontWeight.Bold, modifier=Modifier.padding(40.dp))
                   Button(
                       onClick = {
                           pantallas = 1
                       }
                   ) {
                       Text(
                           text="Otra vez",
                           color = Color.White)
                   }
                Box(
                    modifier = Modifier.align(Alignment.End).padding(30.dp)
                ){
                    Image(
                        bitmap = useResource("${jugador.nombre}.png") {
                            loadImageBitmap(it)
                        }, "${jugador.nombre}",
                        modifier = Modifier.size(200.dp)
                    )
                }
               }
        }

//Cambio de pantallas
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 70.dp),
            verticalArrangement =Arrangement.Center
        ) {
            when (pantallas) {
                1 -> eleccionPokemon(
                    asignar = { jugador = it },
                    asignarMaquina = { maquina = it },
                    pantalla = { pantallas = it })

                2 -> combate(ganador = { ganador = it })
                3 -> finJuego()
            }
        }
    }
}



fun main() = application {

        var fondo=painterResource("campo.png")

        Window(onCloseRequest = ::exitApplication,
            resizable = false,
            state = WindowState(size=WindowSize(1000.dp, 800.dp)),
            title = "POKEMON",
            icon = painterResource("logo.png")
        ) {
            Box(modifier = Modifier.paint(fondo).fillMaxSize()){
                Row(modifier = Modifier.fillMaxWidth(1f)
                ){
                    Image(
                        bitmap = useResource("logo.png") {
                            loadImageBitmap(it)
                        }, "logo",
                        modifier = Modifier.fillMaxWidth(1f))
                }
            }
            App()

        }

}

