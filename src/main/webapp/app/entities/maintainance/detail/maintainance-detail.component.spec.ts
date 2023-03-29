import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaintainanceDetailComponent } from './maintainance-detail.component';

describe('Maintainance Management Detail Component', () => {
  let comp: MaintainanceDetailComponent;
  let fixture: ComponentFixture<MaintainanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaintainanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ maintainance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MaintainanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MaintainanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maintainance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.maintainance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
