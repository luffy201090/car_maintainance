import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CorporationDetailComponent } from './corporation-detail.component';

describe('Corporation Management Detail Component', () => {
  let comp: CorporationDetailComponent;
  let fixture: ComponentFixture<CorporationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CorporationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ corporation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CorporationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CorporationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load corporation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.corporation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
