import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaintainanceDetailsFormService } from './maintainance-details-form.service';
import { MaintainanceDetailsService } from '../service/maintainance-details.service';
import { IMaintainanceDetails } from '../maintainance-details.model';
import { IMaintainance } from 'app/entities/maintainance/maintainance.model';
import { MaintainanceService } from 'app/entities/maintainance/service/maintainance.service';

import { MaintainanceDetailsUpdateComponent } from './maintainance-details-update.component';

describe('MaintainanceDetails Management Update Component', () => {
  let comp: MaintainanceDetailsUpdateComponent;
  let fixture: ComponentFixture<MaintainanceDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maintainanceDetailsFormService: MaintainanceDetailsFormService;
  let maintainanceDetailsService: MaintainanceDetailsService;
  let maintainanceService: MaintainanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MaintainanceDetailsUpdateComponent],
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
      .overrideTemplate(MaintainanceDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaintainanceDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintainanceDetailsFormService = TestBed.inject(MaintainanceDetailsFormService);
    maintainanceDetailsService = TestBed.inject(MaintainanceDetailsService);
    maintainanceService = TestBed.inject(MaintainanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Maintainance query and add missing value', () => {
      const maintainanceDetails: IMaintainanceDetails = { id: 456 };
      const maintainance: IMaintainance = { id: 79461 };
      maintainanceDetails.maintainance = maintainance;

      const maintainanceCollection: IMaintainance[] = [{ id: 28266 }];
      jest.spyOn(maintainanceService, 'query').mockReturnValue(of(new HttpResponse({ body: maintainanceCollection })));
      const additionalMaintainances = [maintainance];
      const expectedCollection: IMaintainance[] = [...additionalMaintainances, ...maintainanceCollection];
      jest.spyOn(maintainanceService, 'addMaintainanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintainanceDetails });
      comp.ngOnInit();

      expect(maintainanceService.query).toHaveBeenCalled();
      expect(maintainanceService.addMaintainanceToCollectionIfMissing).toHaveBeenCalledWith(
        maintainanceCollection,
        ...additionalMaintainances.map(expect.objectContaining)
      );
      expect(comp.maintainancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const maintainanceDetails: IMaintainanceDetails = { id: 456 };
      const maintainance: IMaintainance = { id: 44454 };
      maintainanceDetails.maintainance = maintainance;

      activatedRoute.data = of({ maintainanceDetails });
      comp.ngOnInit();

      expect(comp.maintainancesSharedCollection).toContain(maintainance);
      expect(comp.maintainanceDetails).toEqual(maintainanceDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainanceDetails>>();
      const maintainanceDetails = { id: 123 };
      jest.spyOn(maintainanceDetailsFormService, 'getMaintainanceDetails').mockReturnValue(maintainanceDetails);
      jest.spyOn(maintainanceDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainanceDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintainanceDetails }));
      saveSubject.complete();

      // THEN
      expect(maintainanceDetailsFormService.getMaintainanceDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintainanceDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(maintainanceDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainanceDetails>>();
      const maintainanceDetails = { id: 123 };
      jest.spyOn(maintainanceDetailsFormService, 'getMaintainanceDetails').mockReturnValue({ id: null });
      jest.spyOn(maintainanceDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainanceDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintainanceDetails }));
      saveSubject.complete();

      // THEN
      expect(maintainanceDetailsFormService.getMaintainanceDetails).toHaveBeenCalled();
      expect(maintainanceDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintainanceDetails>>();
      const maintainanceDetails = { id: 123 };
      jest.spyOn(maintainanceDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintainanceDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintainanceDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMaintainance', () => {
      it('Should forward to maintainanceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(maintainanceService, 'compareMaintainance');
        comp.compareMaintainance(entity, entity2);
        expect(maintainanceService.compareMaintainance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
