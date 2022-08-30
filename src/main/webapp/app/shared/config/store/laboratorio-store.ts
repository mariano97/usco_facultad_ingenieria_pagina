import { ILaboratorio } from "@/shared/model/laboratorio.model";
import { Module } from "vuex";


export interface ILaboratorioStarable {
  laboratorios: ILaboratorio[];
  hasLabGranja: boolean;
  hasLabMuseo: boolean;
}

export const defaultLaboratorioStore: ILaboratorioStarable = {
  laboratorios: [],
  hasLabGranja: false,
  hasLabMuseo: false,
};

export const laboratoriosStore: Module<ILaboratorioStarable, any> = {
  state: { ...defaultLaboratorioStore },
  getters: {
    hasLabGranja: state => state.hasLabGranja,
    hasLabMuseo: state => state.hasLabMuseo,
    obtenerLaboratorios: state => state,
    obtenerLaboratorioByTipoLab: state => (tipoLabId: number) => state.laboratorios.find(lab => lab.tipoLaboratorio.id === tipoLabId),
  },
  mutations: {
    agregarLaboratorio(state, payload) {
      state.laboratorios.push(payload);
    },
    setLabGranja(state, payload) {
      state.hasLabGranja = payload;
    },
    setLabMuseo(state, payload) {
      state.hasLabMuseo = payload;
    },
  },
};
