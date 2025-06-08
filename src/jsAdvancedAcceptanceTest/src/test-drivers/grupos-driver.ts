import { Grupo } from "../../../main/frontend/src/app/model/grupo";

export interface GruposDriver {
  iniciar(): Promise<void>;
  crearGrupo(nombre: string, miembros: Array<string>): Promise<Grupo>;
  crearConUnUnicoMiembro(): Promise<void>;
  validarNombreDeGrupo(grupo: Grupo): Promise<void>;
  validarGrupoNoExiste(nombre: string): Promise<void>;
  validarMiembrosDeGrupo(grupo: Grupo): Promise<void>;
  validarMensajeDeAlMenosDosMiembros(): Promise<void>;
  validarMontoTotal(montoEsperado: string, grupo:Grupo): Promise<void>;
}
