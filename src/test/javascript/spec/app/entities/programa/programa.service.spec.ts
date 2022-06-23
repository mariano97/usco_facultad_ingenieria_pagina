/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import ProgramaService from '@/entities/programa/programa.service';
import { Programa } from '@/shared/model/programa.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Programa Service', () => {
    let service: ProgramaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ProgramaService();
      currentDate = new Date();
      elemDefault = new Programa(
        123,
        'AAAAAAA',
        0,
        0,
        currentDate,
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        false,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            fechaRegistroCalificado: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Programa', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            fechaRegistroCalificado: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaRegistroCalificado: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Programa', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Programa', async () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            codigoSnies: 1,
            codigoRegistroCalificado: 1,
            fechaRegistroCalificado: dayjs(currentDate).format(DATE_TIME_FORMAT),
            nombreTituloOtorgado: 'BBBBBB',
            numeroCreditos: 1,
            duracionPrograma: 1,
            presentacionPrograma: 'BBBBBB',
            mision: 'BBBBBB',
            vision: 'BBBBBB',
            perfilEstudiante: 'BBBBBB',
            perfilEgresado: 'BBBBBB',
            perfilOcupacional: 'BBBBBB',
            urlFotoPrograma: 'BBBBBB',
            dirigidoAQuien: 'BBBBBB',
            costoPrograma: 1,
            estado: true,
            emailContacto: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistroCalificado: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Programa', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Programa', async () => {
        const patchObject = Object.assign(
          {
            codigoSnies: 1,
            fechaRegistroCalificado: dayjs(currentDate).format(DATE_TIME_FORMAT),
            mision: 'BBBBBB',
            perfilEstudiante: 'BBBBBB',
            urlFotoPrograma: 'BBBBBB',
            dirigidoAQuien: 'BBBBBB',
            costoPrograma: 1,
            emailContacto: 'BBBBBB',
          },
          new Programa()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaRegistroCalificado: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Programa', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Programa', async () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            codigoSnies: 1,
            codigoRegistroCalificado: 1,
            fechaRegistroCalificado: dayjs(currentDate).format(DATE_TIME_FORMAT),
            nombreTituloOtorgado: 'BBBBBB',
            numeroCreditos: 1,
            duracionPrograma: 1,
            presentacionPrograma: 'BBBBBB',
            mision: 'BBBBBB',
            vision: 'BBBBBB',
            perfilEstudiante: 'BBBBBB',
            perfilEgresado: 'BBBBBB',
            perfilOcupacional: 'BBBBBB',
            urlFotoPrograma: 'BBBBBB',
            dirigidoAQuien: 'BBBBBB',
            costoPrograma: 1,
            estado: true,
            emailContacto: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaRegistroCalificado: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Programa', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Programa', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Programa', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
