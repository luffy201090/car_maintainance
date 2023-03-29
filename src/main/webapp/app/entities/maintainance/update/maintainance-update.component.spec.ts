import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaintainanceFormService } from './maintainance-form.service';
import { MaintainanceService } from '../service/maintainance.service';
import { IMaintainance } from '../maintainance.model';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

import { MaintainanceUpdateComponent } from './maintainance-update.component';

describe('Maintainance Management Update Component', () => {
  let comp: MaintainanceUpdateComponent;
  let fixture: ComponentFixture<MaintainanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maintainanceFormService: MaintainanceFormService;
  let maintainanceService: MaintainanceService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MaintainanceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MaintainanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaintainanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintainanceFormService = TestBed.inject(MaintainanceFormService);
    maintainanceService = TestBed.inject(MaintainanceService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const maintainance: IMaintainance = { id: 456 };
      const car: ICar = { id: 72348 };
      maintainance.car = car;

      const carCollection: ICar[] = [{ id: 26991 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintainance });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const maintainance: IMaintainance = { id: 456 };
      const car: ICar = { id: 1042 };
      maintainance.car = car;

      activatedRoute.data = of({ maintainance });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContain(car);
      expect(comp.maintainance).toEqual(maintainance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainance>>();
      const maintainance = { id: 123 };
      jest.spyOn(maintainanceFormService, 'getMaintainance').mockReturnValue(maintainance);
      jest.spyOn(maintainanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintainance }));
      saveSubject.complete();

      // THEN
      expect(maintainanceFormService.getMaintainance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintainanceService.update).toHaveBeenCalledWith(expect.objectContaining(maintainance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainance>>();
      const maintainance = { id: 123 };
      jest.spyOn(maintainanceFormService, 'getMaintainance').mockReturnValue({ id: null });
      jest.spyOn(maintainanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintainance }));
      saveSubject.complete();

      // THEN
      expect(maintainanceFormService.getMaintainance).toHaveBeenCalled();
      expect(maintainanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainance>>();
      const maintainance = { id: 123 };
      jest.spyOn(maintainanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintainanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCar', () => {
      it('Should forward to carService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
