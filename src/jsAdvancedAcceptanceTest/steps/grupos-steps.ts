import { fail } from "assert";
import { createBdd } from "../../main/frontend/node_modules/playwright-bdd";
import { test } from "../src/fixtures";

const { Given, When, Then } = createBdd(test);

Given("que el usuario inició Repartir", async ({ assembly }) => {
  await assembly.grupos.iniciar();
});

When(
  "el usuario crea un grupo indicando el nombre {string}",
  async ({ assembly, world}, nombre) => {
    world.grupo = await assembly.grupos.crearGrupo(generarNombreUnico(nombre),["Victor", "Brenda"]);
  }
);

When(
  "el usuario crea un grupo indicando que sus miembros son {string} y {string}",
  async ({ assembly, world }, miembro1, miembro2) => {
    world.grupo = await assembly.grupos.crearGrupo(generarNombreUnico("Grupo de Prueba"),[miembro1, miembro2]);
  }
);

When("el usuario crea un grupo", async ({ assembly, world}) => {
  world.grupo = await assembly.grupos.crearGrupo(generarNombreUnico("Grupo de 4"),["Guido", "Laura", "Mariano", "Juan Cruz"]);
});

Then(
  "debería visualizar dentro del listado el grupo con total $ {string}",
  async ({ assembly, world}, montoEsperado) => {
    if(!world.grupo){
      fail("Grupo no encontrado");
    }
    await assembly.grupos.validarMontoTotal(montoEsperado, world.grupo);
  }
);

When(
  "el usuario intenta crear un grupo indicando un único miembro",
  async ({ assembly }) => {
    await assembly.grupos.crearConUnUnicoMiembro();
  }
);

Then(
  "debería ser informado que necesita tener al menos dos miembros",
  async ({ assembly }) => {
    await assembly.grupos.validarMensajeDeAlMenosDosMiembros();
  }
);

Then("no debería crear el grupo con un único miembro", async ({ page }) => {});

Then(
  "debería visualizar dentro del listado el grupo con el nombre indicado",
  async ({ assembly, world}) => {
    await assembly.grupos.validarNombreDeGrupo(world.grupo);
  }
);

Then(
  "no debería visualizar dentro del listado el grupo con el nombre indicado",
  async ({ assembly, world}) => {
    await assembly.grupos.validarGrupoNoExiste(world.grupo.nombre);
  }
);

Then(
  "visualiza dentro del listado el grupo con los miembros indicados",
  async ({ assembly, world }) => {
    await assembly.grupos.validarMiembrosDeGrupo(world.grupo);
  }
);

function generarNombreUnico (nombre:string) {
  const fecha = new Date();
  const nombreFecha =`${nombre}_${fecha.getTime()}`; 
  return nombreFecha;
}
