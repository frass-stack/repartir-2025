# language: es

Característica: Crear Grupo para repartir gastos

  Regla: Los grupos están compuestos por al menos dos miembros

    Escenario: No puedo crear un grupo con un único miembro
      Cuando el usuario intenta crear un grupo indicando un único miembro
      Entonces no debería crear el grupo con un único miembro

  Regla: Los nombres de grupo no se pueden repetir
      
    Escenario: No puedo crear un grupo con nombre repetido
      Dado que existe un grupo con nombre "Viaje a Bariloche"
      Cuando el usuario intenta crear otro grupo con nombre "Viaje a Bariloche"
      Entonces debería recibir un error por nombre repetido
