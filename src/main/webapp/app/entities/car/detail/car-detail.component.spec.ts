import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarDetailComponent } from './car-detail.component';

describe('Car Management Detail Component', () => {
  let comp: CarDetailComponent;
  let fixture: ComponentFixture<CarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ car: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load car on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.car).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
