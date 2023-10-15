package app.jsapps.whatsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Usuario(
    val nombre:String,
    val email:String,
    val estado:String = "¡Hola! Estoy usando iMessage.",
    val fotoPerfil:String = ""
):Parcelable
