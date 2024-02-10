package com.example.swiftlearn.ui.screens.utils

/**
 * Clase que representa los diferentes tipos de contenido de la pantalla en función de su tamaño.
 *
 * - [ListOnly]: Indica que se debe mostrar la lista de anuncios y los detalles en pantallas distintas.
 * - [ListAndDetail]: Indica que se deben mostrar tanto la lista como los detalles del anuncio en la misma pantalla.
 */
enum class AdvertsContentType {
    ListOnly,
    ListAndDetail
}