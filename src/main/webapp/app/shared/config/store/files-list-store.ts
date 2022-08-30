import { Module } from 'vuex';

export interface IFileGoogleStorageDownLoaded {
  fileName?: string;
  file?: any;
  codeGenration?: number;
}

export interface IFileGoogleStorageDownLoadedStarable {
  fileListGoogleStorage: IFileGoogleStorageDownLoaded[];
}

export const defaultFileGoogleStorageDownLoaded: IFileGoogleStorageDownLoadedStarable = {
  fileListGoogleStorage: [],
};

export const fileGoogleStorageDownloadedStore: Module<IFileGoogleStorageDownLoadedStarable, any> = {
  state: { ...defaultFileGoogleStorageDownLoaded },
  getters: {
    obtenerListaFile: state => state,
    obtenerFileByFileName: state => (fileNmae: string) => state.fileListGoogleStorage.find(file => file.fileName === fileNmae),
    existeFileInList: state => (fileName: string) => {
      const listaFilesFilter = state.fileListGoogleStorage.filter(file => file.fileName === fileName);
      return listaFilesFilter.length > 0;
    },
  },
  mutations: {
    agregarGoogleFileToList(state, payload) {
      state.fileListGoogleStorage.push({
        file: payload.file,
        fileName: payload.fileName,
        codeGenration: payload.codeGenration,
      });
    },
    actualizarGoogleFileInList(state, payload) {
      let indexFile = -1;
      if (payload.fileName && payload.codeGenration) {
        indexFile = state.fileListGoogleStorage.findIndex(
          file => file.fileName === payload.fileName && file.codeGenration === payload.codeGenration
        );
      } else if (payload.fileName) {
        indexFile = state.fileListGoogleStorage.findIndex(file => file.fileName === payload.fileName);
      }
      if (indexFile >= 0 && state[indexFile]) {
        state[indexFile].file = payload.file;
      }
    },
  },
};
