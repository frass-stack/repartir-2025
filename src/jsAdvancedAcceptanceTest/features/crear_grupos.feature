# language: es
Característica: Crear Grupo para repartir gastos

  Regla: Los grupos tienen un nombre que los identifica

    Escenario: Crea un grupo con nombre
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo indicando el nombre 'Regalo de navidad'
      Entonces debería visualizar dentro del listado el grupo con el nombre indicado

  Regla: el nombre de un grupo conformado debe tener dos o más caracteres

    Escenario: No puedo crear un grupo con un nombre compuesto por un único caracter
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo indicando el nombre "G"
      Entonces no debería visualizar dentro del listado el grupo con el nombre indicado

    Escenario: No puedo crear un grupo que tiene como nombre ""
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo indicando el nombre ""
      Entonces no debería visualizar dentro del listado el grupo con el nombre indicado

    Escenario: Crea un grupo con nombre de dos caracteres
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo indicando el nombre "lp"
      Entonces debería visualizar dentro del listado el grupo con el nombre indicado

  Regla: Los grupos están compuestos por al menos dos miembros

    Escenario: Crea un grupo con dos miembros
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo indicando que sus miembros son 'mariano' y 'juan cruz'
      Entonces visualiza dentro del listado el grupo con los miembros indicados

    Escenario: No puedo crear un grupo con un único miembro
      Dado que el usuario inició Repartir
      Cuando el usuario intenta crear un grupo indicando un único miembro
      Entonces no debería crear el grupo con un único miembro
      Y debería ser informado que necesita tener al menos dos miembros

  Regla: Los grupos tienen un estado inicial

    Escenario: El total inicial del grupo es $ 0
      Dado que el usuario inició Repartir
      Cuando el usuario crea un grupo
      Entonces debería visualizar dentro del listado el grupo con total $ '0'