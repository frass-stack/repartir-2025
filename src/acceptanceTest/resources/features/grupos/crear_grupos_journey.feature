# language: es
Característica: Experiencia al crear grupos para repartir gastos

  @journey
  Escenario: Crear el primer grupo para la cena de navidad
    * no existe ningún grupo
    * el usuario inicia la aplicación
    * el usuario selecciona crear grupo
    * completa con el nombre 'Cena de navidad'
    * indica que los miembros son 'lucas', 'karen' y 'rocio'
    * guarda el grupo
    * se muestra 1° el grupo 'Cena de navidad' con total '$  0,00'

  @journey
  Escenario: Crear el segundo grupo para el campamento
    * existe un grupo
    * el usuario inicia la aplicación
    * el usuario selecciona crear grupo
    * completa con el nombre 'Campamento en Chascomús'
    * indica que los miembros son:
      | virginia |
      | santiago |
      | magui    |
      | gian     |
      | teo      |
      | mariano  |
    * guarda el grupo
    * se muestra 2° el grupo 'Campamento en Chascomús' con total '$  0,00'
