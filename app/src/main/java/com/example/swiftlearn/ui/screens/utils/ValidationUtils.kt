package com.example.swiftlearn.ui.screens.utils

import android.util.Patterns
import java.util.regex.Pattern

/**
 * [ValidationUtils] es un objeto con un conjunto de validaciones de campos.
 */
object ValidationUtils {
    /**
     * Verifica si un número de teléfono es válido.
     */
    fun isPhoneValid(phone: String): Boolean {
        return phone.trim().isNotEmpty() && Patterns.PHONE.matcher(phone).matches()
    }

    /**
     * Verifica si un código postal es válido.
     */
    fun isPostalValid(postal: String): Boolean {
        return postal.trim().isNotEmpty() && Pattern.compile("^\\d{5}$").matcher(postal).matches()
    }

    /**
     * Verifica si un correo electrónico es válido.
     */
    fun isEmailValid(email: String): Boolean {
        return email.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Verifica si una contraseña es válida.
     */
    fun isPasswordValid(password: String): Boolean {
        return password.trim().isNotEmpty() && password.length >= 6
    }

    /**
     * Verifica si la confirmación de la contraseña coincide con la contraseña.
     */
    fun isConfirmPasswordValid(password:String, confirmPassword: String): Boolean {
        return password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty() && password == confirmPassword
    }

    /**
     * Verifica si una asignatura es válida.
     */
    fun isSubjectValid(subject: String): Boolean {
        return subject.trim().isNotEmpty() && subject.trim().length <= 20
    }

    /**
     * Verifica si un precio es válido.
     */
    fun isPriceValid(price: Int): Boolean {
        return price > 0
    }

    /**
     * Verifica si una descripción es válida.
     */
    fun isDescriptionValid(description: String): Boolean {
        return description.trim().isNotEmpty() && description.trim().length <= 300
    }

    /**
     * Verifica si se selecciona al menos una opción de un conjunto de opciones.
     */
    fun isOptionValid(set: Set<*>): Boolean {
        return set.isNotEmpty()
    }
}