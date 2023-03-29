import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaintainanceDetailsDetailComponent } from './maintainance-details-detail.component';

describe('MaintainanceDetails Management Detail Component', () => {
  let comp: MaintainanceDetailsDetailComponent;
  let fixture: ComponentFixture<MaintainanceDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaintainanceDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ maintainanceDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MaintainanceDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MaintainanceDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maintainanceDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.maintainanceDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
