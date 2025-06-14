import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Grupo } from 'src/app/model/grupo';
import { IdentificarGrupoPipe } from 'src/app/pipes/identificar-grupo.pipe';
import { GrupoService } from '../../services/grupo.service';

@Component({
  selector: 'app-grupo-nuevo',
  templateUrl: './grupo-nuevo.component.html',
  styleUrls: ['./grupo-nuevo.component.css'],
})
export class GrupoNuevoComponent implements OnInit {

  mostrar: boolean = false;
  nombre: string = '';
  miembros: string[] = [];

  @Output() readonly guardadoEvent = new EventEmitter<void>();

  constructor(
    private grupoService: GrupoService,
    private messageService: MessageService,
    private identificarGrupo: IdentificarGrupoPipe) {

  }

  ngOnInit(): void {

  }

  iniciar(): void {

    this.nombre = '';
    this.miembros = [];
    this.mostrar = true;
  }

  // guardar(): void {
  //   this.grupoService.crear(this.nombre, this.miembros).subscribe(
  //     grupo => this.guardadoExitoso(grupo),
  //     error => this.guardadoFallido(error)
  //   );
  // }

  cancelar(): void {

    this.mostrar = false;
  }

  iniciarEdicion(grupo: Grupo): void {
    this.nombre = grupo.nombre;
    this.miembros = grupo.miembros ? [...grupo.miembros] : [];
    this.grupoEditandoId = grupo.id;
    this.mostrar = true;
  }

  grupoEditandoId?: number;

  guardar(): void {
    if (this.grupoEditandoId != null) {
      this.grupoService.actualizar(this.grupoEditandoId, this.nombre, this.miembros).subscribe(
        grupo => this.guardadoExitoso(grupo),
        error => this.guardadoFallido(error)
      );
    } else {
      this.grupoService.crear(this.nombre, this.miembros).subscribe(
        grupo => this.guardadoExitoso(grupo),
        error => this.guardadoFallido(error)
      );
    }
  }

  private guardadoExitoso(grupo: Grupo):void {
    this.messageService.add({
      severity: 'success',
      summary: 'Éxito',
      detail: `Grupo '${this.identificarGrupo.transform(grupo)}' creado`,
    });

    this.guardadoEvent.emit();
    this.mostrar = false;
  }

  private guardadoFallido(error: any): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: error.mensaje,
    });
  }
}
