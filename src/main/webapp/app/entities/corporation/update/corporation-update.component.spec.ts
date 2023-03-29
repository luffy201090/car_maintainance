import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CorporationFormService } from './corporation-form.service';
import { CorporationService } from '../service/corporation.service';
import { ICorporation } from '../corporation.model';

import { CorporationUpdateComponent } from './corporation-update.component';

describe('Corporation Management Update Component', () => {
  let comp: CorporationUpdateComponent;
  let fixture: ComponentFixture<CorporationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let corporationFormService: CorporationFormService;
  let corporationService: CorporationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CorporationUpdateComponent],
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
      .overrideTemplate(CorporationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CorporationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    corporationFormService = TestBed.inject(CorporationFormService);
    corporationService = TestBed.inject(CorporationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const corporation: ICorporation = { id: 456 };

      activatedRoute.data = of({ corporation });
      comp.ngOnInit();

      expect(comp.corporation).toEqual(corporation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorporation>>();
      const corporation = { id: 123 };
      jest.spyOn(corporationFormService, 'getCorporation').mockReturnValue(corporation);
      jest.spyOn(corporationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corporation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corporation }));
      saveSubject.complete();

      // THEN
      expect(corporationFormService.getCorporation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(corporationService.update).toHaveBeenCalledWith(expect.objectContaining(corporation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorporation>>();
      const corporation = { id: 123 };
      jest.spyOn(corporationFormService, 'getCorporation').mockReturnValue({ id: null });
      jest.spyOn(corporationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corporation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corporation }));
      saveSubject.complete();

      // THEN
      expect(corporationFormService.getCorporation).toHaveBeenCalled();
      expect(corporationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorporation>>();
      const corporation = { id: 123 };
      jest.spyOn(corporationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corporation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(corporationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
