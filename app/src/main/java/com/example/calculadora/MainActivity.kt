package com.example.calculadora

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var addOperacion = false
    private var addDecimal = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            binarioButton.setOnClickListener(View.OnClickListener {
                dosButton.isEnabled = false
                tresButton.isEnabled = false
                cuatroButton.isEnabled = false
                cincoButton.isEnabled = false
                seisButton.isEnabled = false
                sieteButton.isEnabled = false
                ochoButton.isEnabled = false
                nueveButton.isEnabled = false
                puntoButton.isEnabled = false
                aButton.isEnabled = false
                bButton.isEnabled = false
                cButton.isEnabled = false
                dButton.isEnabled = false
                eButton.isEnabled = false
                fButton.isEnabled = false

                unoButton.setOnClickListener {
                    numberActionB(unoButton)
                }
                ceroButton.setOnClickListener {
                    numberActionB(ceroButton)
                }
                sumarButton.setOnClickListener {
                    operacionActionB(sumarButton)
                }
                restarButton.setOnClickListener {
                    operacionActionB(restarButton)
                }
                multiplicarButton.setOnClickListener {
                    operacionActionB(multiplicarButton)
                }
                dividirButton.setOnClickListener {
                    operacionActionB(dividirButton)
                }
                igualButton.setOnClickListener {
                    igualActionB(igualButton)
                }
                borrarButton.setOnClickListener {
                    borrarAction(borrarButton)
                }


            })
            decimalButton.setOnClickListener(View.OnClickListener {
            dosButton.isEnabled = true
            tresButton.isEnabled = true
            cuatroButton.isEnabled = true
            cincoButton.isEnabled = true
            seisButton.isEnabled = true
            sieteButton.isEnabled = true
            ochoButton.isEnabled = true
            nueveButton.isEnabled = true
            puntoButton.isEnabled = true
            aButton.isEnabled = false
            bButton.isEnabled = false
            cButton.isEnabled = false
            dButton.isEnabled = false
            eButton.isEnabled = false
            fButton.isEnabled = false
        })
            hexadecimalButton.setOnClickListener(View.OnClickListener {
            puntoButton.isEnabled = false
            aButton.isEnabled = true
            bButton.isEnabled = true
            cButton.isEnabled = true
            dButton.isEnabled = true
            eButton.isEnabled = true
            fButton.isEnabled = true
        })

        }
    }
    //funcion que detecta si el boton es un ., para asi poder introducir decimales
    //de no serlo muestra el numero pulsado y permite crear operaciones
    fun numberAction(view: View){
        if(view is Button){
            if (view.text == "."){
                if (addDecimal){
                    operacionesTextView.append(view.text)
                    addDecimal = false
                }
            }
            else
                operacionesTextView.append(view.text)
            addOperacion = true
        }
    }
    //funcion que te permite añadir las operaciones si pulsas sobre ella y que controla que solo se pueda introducir un simbolo de operacion
    fun operacionAction(view: View){
        if(view is Button && addOperacion){
            operacionesTextView.append(view.text)
            addOperacion = false
            addDecimal = true
        }
    }
    //funcion que llama a calcularResultados par asi al pulsar sobre el igual calcula lo introducido y lo muestra en resultadoTextView
    fun igualAction(view: View){
        resultadoTextView.text = calcularResultados()
    }
    //funcion que llama a tres operaciones distintas dependiendo de la operacion introducida y asi si esa opecaion no ha sido utilizada de devuelve vacio
    //son constantes ya que estas no deben cambiar en el proceso de ejecucion
     fun calcularResultados(): String {
        val digitosOperador = digitosOperador()
        if(digitosOperador.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitosOperador)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }
    //funcion que calcula la suma y resta y nos devuelve un flotante pasandole una lista con todos los digitos introducidos
    //que va recorriendo nuestra lista caracter a caracter y sumando o restando, al encontrarse con un + o con un -, opera con los digitos anteriores y siguientes
     fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == '+')
                    result += nextDigit

                if(operator == '-')
                    result -= nextDigit

            }
        }

        return result
    }
    //funcion que recorre la lista en busca de los simbolos de multiplicar o restar y al encontralos llama a calcTimesDiv para realizar las operaciones correspondientes
     fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('*') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }
    //funcion que recorre la lista  caracter a caracter hasta llegar al final de esta y cuando encutra un * o un /, guarda el valor anterior y posterior y los multiplica o divide, guardados en variables como Float
    //de no encontrar estos simbolos simplemente recorre y guarda los valores de la lista
     fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i !=passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator)
                {
                    '*' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }
    //funcion que sirve para guardar lo introducido por los botones en una lista ya sea un numero o un . y los convierte en float
     fun digitosOperador(): MutableList<Any>{

        val list = mutableListOf<Any>()
        var currentDigital = ""

        for(character in operacionesTextView.text)
        {
            if(character.isDigit() || character == '.')
                currentDigital += character
            else
            {
                list.add(currentDigital.toFloat())
                currentDigital = ""
                list.add(character)
            }
        }
        if(currentDigital!="")
            list.add(currentDigital.toFloat())

        return list

    }
    //funcion que resetea los valores de los resultados
    fun borrarAction(view: View){
        resultadoTextView.text = ""
        operacionesTextView.text = ""
    }

    fun convertBinaryToDecimal(num: Long): Int {
        var num = num
        var decimalNumber = 0
        var i = 0
        var remainder: Long

        while (num.toInt() != 0) {
            remainder = num % 10
            num /= 10
            decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
            ++i
        }
        return decimalNumber
    }

    //BINARIO
    //funcion que detecta si el boton es un ., para asi poder introducir decimales
    //de no serlo muestra el numero pulsado y permite crear operaciones
    fun numberActionB(view: View){
        if(view is Button){
            if (view.text == "."){
                if (addDecimal){
                    operacionesTextView.append(view.text)
                    addDecimal = false
                }
            }
            else
                operacionesTextView.append(view.text)
            addOperacion = true
        }
    }
    //funcion que te permite añadir las operaciones si pulsas sobre ella y que controla que solo se pueda introducir un simbolo de operacion
    fun operacionActionB(view: View){
        if(view is Button && addOperacion){
            operacionesTextView.append(view.text)
            addOperacion = false
            addDecimal = true
        }
    }
    //funcion que llama a calcularResultados par asi al pulsar sobre el igual calcula lo introducido y lo muestra en resultadoTextView
    fun igualActionB(view: View){
        resultadoTextView.text = calcularResultadosB()
    }
    //funcion que llama a tres operaciones distintas dependiendo de la operacion introducida y asi si esa opecaion no ha sido utilizada de devuelve vacio
    //son constantes ya que estas no deben cambiar en el proceso de ejecucion
    fun calcularResultadosB(): String {
        val digitsOperator = digitosOperadorB()
        if(digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculateB(digitsOperator)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculateB(timesDivision)
        return result.toString()
    }
    //funcion que calcula la suma y resta y nos devuelve un flotante pasandole una lista con todos los digitos introducidos
    //que va recorriendo nuestra lista caracter a caracter y sumando o restando, al encontrarse con un + o con un -, opera con los digitos anteriores y siguientes
    fun addSubtractCalculateB(passedList: MutableList<Any>): Int {
        val result = passedList[0] as Long
        var num = convertBinaryToDecimal(result)
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Long
                val num2 = convertBinaryToDecimal(nextDigit)
                if(operator == '+')

                    num += num2

                if(operator == '-')
                    num -= num2

            }
        }
        Integer.toBinaryString(num)
        return num
    }
    //funcion que recorre la lista en busca de los simbolos de multiplicar o restar y al encontralos llama a calcTimesDiv para realizar las operaciones correspondientes
    fun timesDivisionCalculateB(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('*') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }
    //funcion que recorre la lista  caracter a caracter hasta llegar al final de esta y cuando encutra un * o un /, guarda el valor anterior y posterior y los multiplica o divide, guardados en variables como Float
    //de no encontrar estos simbolos simplemente recorre y guarda los valores de la lista
    fun calcTimesDivB(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i !=passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Long
                val num = convertBinaryToDecimal(prevDigit)
                val nextDigit = passedList[i+1] as Long
                val num2 = convertBinaryToDecimal(nextDigit)
                when(operator)
                {
                    '*' ->
                    {   val result=num * num2
                        Integer.toBinaryString(result)
                        newList.add(result)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                        var result=num / num2
                        Integer.toBinaryString(result)
                        newList.add(result)
                        restartIndex = i+1
                    }
                    else ->{
                        newList.add(num)
                        newList.add(num2)
                    }

                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }
    //funcion que sirve para guardar lo introducido por los botones en una lista ya sea un numero o un . y los convierte en float
    fun digitosOperadorB(): MutableList<Any>{

        val list = mutableListOf<Any>()
        var currentDigital = ""

        for(character in operacionesTextView.text)
        {
            if(character.isDigit() || character == '.')
                currentDigital += character
            else
            {
                list.add(currentDigital.toFloat())
                currentDigital = ""
                list.add(character)
            }
        }
        if(currentDigital!="")
            list.add(currentDigital.toFloat())

        return list

    }
    //funcion que resetea los valores de los resultados
    fun borrarActionB(view: View){
        resultadoTextView.text = ""
        operacionesTextView.text = ""
    }
}