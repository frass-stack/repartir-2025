import {
  Page,
  expect,
} from "../../../main/frontend/node_modules/playwright/test";
import { Grupo } from "../../../main/frontend/src/app/model/grupo";
import { GruposDriver } from "./grupos-driver";

export class GruposPlaywrightDriver implements GruposDriver {

  constructor(private page: Page) {}

  iniciar = async (): Promise<void> => {
    await this.page.goto("/");
    await this.page.getByRole("textbox").fill("julian");
    await this.page.locator("#iniciarBienvenidaButton").click();
  };

  crearConUnUnicoMiembro = async (): Promise<void> => {
    await this.page.locator("#crearGruposButton").click();
    await this.page.locator("#nombreGrupoNuevoInput").fill("Grupo inválido");
    await this.page.locator("#miembrosGrupoNuevoInput").fill("Oscar");
    await this.page.keyboard.press("Enter");

    await this.page.locator("#guardarGrupoNuevoButton").click();
  };

  crearGrupo = async (
    nombre: string,
    miembros: Array<string>
  ): Promise<Grupo> => {
    const gruposAntesDeCrearUnoNuevo = await this.page
      .locator("app-grupos table tr")
      .count();

    await this.page.locator("#crearGruposButton").click();
    await this.page.locator("#nombreGrupoNuevoInput").fill(nombre);

    for (let i = 0; i < miembros.length; i++) {
      await this.page.locator("#miembrosGrupoNuevoInput").fill(miembros[i]);
      await this.page.keyboard.press("Enter");
    }

    await this.page.locator("#guardarGrupoNuevoButton").click();

    await this.page.waitForFunction((gruposAntesDeCrearUnoNuevo) => {
      const gruposAhora = document.querySelectorAll(
        "app-grupos table tr"
      ).length;
      return gruposAhora > gruposAntesDeCrearUnoNuevo;
    }, gruposAntesDeCrearUnoNuevo);

    const grupoFila = await this.page.locator("app-grupos table tr", {
      hasText: nombre,
    });
    const grupoId = await grupoFila.locator("td:nth-child(1)").textContent();

    return  {
      id: grupoId ? parseInt(grupoId) : -1,
      nombre: nombre,
      miembros: miembros,
    }
  };

  validarNombreDeGrupo = async (grupo: Grupo): Promise<void> => {
    await expect(this.page.getByRole("alert")).toContainText(
      grupo.nombre
    );
  };

  validarGrupoNoExiste = async (nombre: string): Promise<void> => {
    const grupoFila = await this.page.locator("app-grupos table tr", {
      hasText: nombre,
    });
    await expect(grupoFila).toHaveCount(0);
  };

  validarMiembrosDeGrupo = async (grupo: Grupo): Promise<void> => {
    let row = this.page
      .locator(
        `app-grupos table tr:has(td:nth-child(2):text("${grupo.nombre}"))`
      )
      .last();
    let miembros = await row.locator("td:nth-child(4)");

    for (let index = 0; index < grupo.miembros.length; index++) {
      await expect(miembros).toContainText(
        grupo.miembros[index]
      );
    }
  };

  validarMensajeDeAlMenosDosMiembros = async (): Promise<void> => {
    let mensajesToast = this.page.getByRole("alert");
    await mensajesToast.waitFor({ state: "visible", timeout: 2000 });
    await expect(mensajesToast).toContainText("Error");
  };

  validarMontoTotal = async (
    montoEsperado: string,
    grupo: Grupo
  ): Promise<void> => {
    const filaConGrupoId = await this.page.locator("app-grupos table tr", {
      hasText: grupo.nombre,
    });
    let monto = await filaConGrupoId.locator("td:nth-child(3)");

    await expect(monto).toContainText(montoEsperado);
  };
}
