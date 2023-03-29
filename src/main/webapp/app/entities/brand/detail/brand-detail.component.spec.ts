import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BrandDetailComponent } from './brand-detail.component';

describe('Brand Management Detail Component', () => {
  let comp: BrandDetailComponent;
  let fixture: ComponentFixture<BrandDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BrandDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ brand: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BrandDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BrandDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load brand on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.brand).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
