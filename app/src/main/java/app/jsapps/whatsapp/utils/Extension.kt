package app.jsapps.whatsapp.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.viewmodel.UserViewModel
import com.google.firebase.database.DataSnapshot


fun AppCompatActivity.toast(text: String) {

    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun DataSnapshot.toUser() = Usuario(
    nombre = this.child("nombre").value.toString(),
    estado = this.child("estado").value.toString(),
    fotoPerfil = this.child("fotoPerfil").value.toString(),
    email = this.child("email").value.toString()

)

fun DataSnapshot.toContacts() = Usuario(
    nombre = this.child("nombre").value.toString(),
    email = this.child("email").value.toString()

)

fun DataSnapshot.toMessage() = Message(
    author = this.child("author").value.toString(),
    datetime = this.child("datetime").value.toString(),
    message = this.child("message").value.toString(),
    timeStamp = this.child("timeStamp").value.toString().toInt()

)

fun UserViewModel.emptyUser() = Usuario("", "", "")

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}

fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}