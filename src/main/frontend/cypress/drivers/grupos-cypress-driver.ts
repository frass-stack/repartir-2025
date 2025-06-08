import { GruposDriver } from "../../../../jsAdvancedAcceptanceTest/src/test-drivers/grupos-driver";
import { Grupo } from "../../src/app/model/grupo";

export class GruposCypressDriver implements GruposDriver {

    async iniciar(): Promise<void> {
        cy.visit("/");
        cy.get('#usuarioInput').type("julian");
        cy.get('#iniciarBienvenidaButton').click();
    }

    async crearGrupo(nombre: string, miembros: Array<string>): Promise<Grupo> {
        cy.get("#crearGruposButton").click();
        cy.get("#nombreGrupoNuevoInput").type(nombre);

        for (let i = 0; i < miembros.length; i++) {
            cy.get("#miembrosGrupoNuevoInput").type(`${miembros[i]}{enter}`);
        }
        cy.get("#guardarGrupoNuevoButton").click();

        return {
            nombre: nombre,
            miembros: miembros,
        };
    }

    async crearConUnUnicoMiembro(): Promise<void> {
        cy.get("#crearGruposButton").click();
        cy.get("#nombreGrupoNuevoInput").type('Grupo inválido');
        cy.get("#miembrosGrupoNuevoInput").type('Oscar{enter}');
        cy.get("#guardarGrupoNuevoButton").click();
    }

    async validarNombreDeGrupo(grupo: Grupo): Promise<void> {
        cy.get('table tbody tr').filter((_, element) => {
            return Cypress.$(element).text().includes(grupo.nombre);
        }).then((grupoEncontrado) => {
            expect(grupoEncontrado).to.exist;
        })
    }

    async validarGrupoNoExiste(nombre: string): Promise<void> {
        cy.get('table tbody tr').filter((_, element) => {
            return Cypress.$(element).text().includes(nombre);
        }).then((grupoEncontrado) => {
            expect(grupoEncontrado).to.not.exist;
        })
    }

    async validarMiembrosDeGrupo(grupo: Grupo): Promise<void> {
        cy.get('table tbody tr').filter((_, element) => {
            return Cypress.$(element).text().includes(grupo.nombre);
        }).then((grupoEncontrado) => {
            for (let i = 0; i < grupo.miembros.length; i++) {
                cy.wrap(grupoEncontrado).find('p-chip').contains(grupo.miembros[i]).should('exist');
            }
        })
    }
    async validarMensajeDeAlMenosDosMiembros(): Promise<void> {
        cy.get('[role=alert]').then((elemento) => {
            expect(elemento.text()).to.contain('Error')
        });
    }

    async validarMontoTotal(montoEsperado: string, grupo: Grupo): Promise<void> {
        cy.get('table tbody tr').filter((index, element) => {
            return Cypress.$(element).text().includes(grupo.nombre);
        }).then((grupoEncontrado) => {
            expect(grupoEncontrado).to.exist;
            let montoEnString = grupoEncontrado.find('td').eq(2).text().trim().replace(/\s+/g, ' ').match(/\d+/);
            let monto = montoEnString ? montoEnString[0] : undefined;
            expect(monto).to.equal(montoEsperado);
        });
    }
}