package com.example.swiftlearn.ui.screens

import android.accounts.AuthenticatorDescription
import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {
    fun isPhoneValid(phone: String): Boolean {
        return phone.trim().isNotEmpty() && Patterns.PHONE.matcher(phone).matches()
    }

    fun isPostalValid(postal: String): Boolean {
        return postal.trim().isNotEmpty() && Pattern.compile("^\\d{5}$").matcher(postal).matches()
    }

    fun isEmailValid(email: String): Boolean {
        return email.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.trim().isNotEmpty() && password.length >= 6
    }

    fun isConfirmPasswordValid(password:String, confirmPassword: String): Boolean {
        return password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty() && password == confirmPassword
    }

    fun isSubjectValid(subject: String): Boolean {
        return subject.trim().isNotEmpty() && subject.trim().length <= 20
    }

    fun isPriceValid(price: Int): Boolean {
        return price > 0
    }

    fun isDescriptionValid(description: String): Boolean {
        return description.trim().isNotEmpty() && description.trim().length <= 300
    }

    fun isOptionValid(set: Set<*>): Boolean {
        return set.isNotEmpty()
    }
}