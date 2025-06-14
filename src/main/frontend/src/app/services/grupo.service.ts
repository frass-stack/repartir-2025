import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Grupo } from '../model/grupo';
import { Excepcion } from '../model/excepcion';
import { Gasto } from '../model/gasto';

@Injectable({
  providedIn: 'root'
})
export class GrupoService {

  private baseUrl = '/api/grupos';

  constructor(private http: HttpClient) {

  }

  listar(): Observable<Grupo[]> {

    return this.http.get<Grupo[]>('/api/grupos')
      .pipe(catchError(this.falloAlListar));
  }

  crear(nombre: string, miembros: string[]): Observable<Grupo> {

    const nuevoGrupo: Grupo = {
      nombre,
      miembros
    };

    return this.http.post<Grupo>('/api/grupos', nuevoGrupo)
      .pipe(catchError(this.falloAlGuardar));
  }

  agregarGasto(grupo: Grupo, monto: number): Observable<Grupo> {

    const nuevoGasto: Gasto = {
      monto,
    }

    return this.http.post<Grupo>(`/api/grupos/${grupo.id}/gastos`, nuevoGasto)
      .pipe(catchError(this.falloAlGuardar));
  }

  falloAlGuardar(error: HttpErrorResponse) {
    return throwError(new Excepcion(error.error));
  }

  falloAlListar(error: HttpErrorResponse) {
    return throwError(new Excepcion("No se pueden listar los Grupos"));
  }

  actualizar(id: number, nombre: string, miembros: string[]): Observable<Grupo> {
    const payload = { nombre, miembros };
    return this.http.put<Grupo>(`${this.baseUrl}/${id}`, payload)
      .pipe(catchError(this.falloAlGuardar));
  }
}
