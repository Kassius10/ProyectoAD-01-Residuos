## Autores: Jeremy Ramos y Daniel Carmona

# ProyectoAD-01-Residuos

### Propuesta de solución
Nos hemos planteado una solucil lo mas correlacionada a nuestros conocimientos y utilizando tecnologías varias las cuales se verán en la travesía del programa para trabajar con transformadores del contenido CSV a una opción legible para el programa. También como veremos posteriormente implementaremos el trabajar con los datos de forma organizada con mecanismos de consultas tales como DataFrames. 

## Disposicion de clases

### Controladores
Se encargan de manejar el funcionamiento del programa de forma ordenada y encapsulada en varios mecanismos distribuidos sobre las mismas.

#### ContenedorControler/ResiduosController
Clases las cuales trabajan con el trato de los dos csv’s introducidos tales como pasarlos a los distintos formatos CSV, XML y Json. Trabajando como veremos posteriormente con el trato de DTO´s  

### Exceptions
Pequeña clase la cual hemos abstraído un modelo o tipo de excepción.

### Mappers
Clase con la cual tratamos los datos para transformarlos en DTO y viceversa de forma organizada

### Models
Clases donde manejamos la entrada y parametrización de los datos tales como los campos del csv, y dentro del csv encontramos datos relevantes de forma monótona y para esto hemos hecho clases enums las cuales están repartidas dentro de las clases correspondientes.

### Utils
Paquete que contiene clases de utilidad las cuales nos permiten hacer distintas cosas tales como: Generar HTML, ciertos parseadores y formateadores de formatos tales como LocalDate y Formateo de Strings etc.

## Main(Clase Principal)
Clase principal donde nuestro programa se construye de forma progresiva y gestiona los mecanismos de entrada a nuestro programa los cuales son: parser, resumen y resumen por x Distrito. 
